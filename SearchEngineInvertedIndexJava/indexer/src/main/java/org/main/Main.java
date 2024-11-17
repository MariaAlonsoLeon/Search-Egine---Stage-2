package org.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.main.inverted_index.*;
import org.main.metadata.*;

public class Main {
    private static final String DOCUMENT_REPOSITORY = "datalake/";
    private static String currentDate;
    private static final ProcessMetadata process_metadata = new ProcessMetadata();
    private static final ProcessInvertedIndex process_inverted_index = new ProcessInvertedIndex();
    private static final StoreText_II storeTextII = new StoreText_II();
    private static final StoreBinary_II storeBinaryFileII = new StoreBinary_II();
    private static final StoreMongoDB_II store_mongoDB_II = new StoreMongoDB_II();
    private static final StoreNeo4j_II storeNeo4jII = new StoreNeo4j_II();
    private static final StoreBinary_MD storeBinaryFileMD = new StoreBinary_MD();
    private static final StoreMongoDB_MD store_mongoDB_MD = new StoreMongoDB_MD();
    private static final StoreNeo4j_MD storeNeo4jMD = new StoreNeo4j_MD();
    private static final StoreText_MD storeTextMD = new StoreText_MD();

    public static void main(String[] args) {
        String datatype = System.getenv("DATATYPE");
        System.out.println("DATATYPE: " + datatype);

        updateDate();
        scheduleMidnightUpdate();

        Map<String, Consumer<DocumentProcessors>> actions = new HashMap<>();
        actions.put("Text", processors -> {
            processors.storeInvertedIndex(storeTextII::storeInvertedIndex);
            processors.storeMetadata(storeTextMD::storeMetadata);
        });
        actions.put("BinaryFile", processors -> {
            processors.storeInvertedIndex(storeBinaryFileII::storeInvertedIndex);
            processors.storeMetadata(storeBinaryFileMD::storeMetadata);
        });
        actions.put("MongoDB", processors -> {
            processors.storeInvertedIndex(store_mongoDB_II::storeInvertedIndex);
            processors.storeMetadata(store_mongoDB_MD::storeMetadata);
        });
        actions.put("NEO4J", processors -> {
            processors.storeInvertedIndex(storeNeo4jII::storeInvertedIndex);
            processors.storeMetadata(storeNeo4jMD::storeMetadata);
        });

        if (datatype != null && actions.containsKey(datatype)) {
            processAllDocumentsInDatalake(DOCUMENT_REPOSITORY, actions.get(datatype));

            listener(DOCUMENT_REPOSITORY, storeNeo4jII, storeNeo4jMD);
        } else {
            System.out.println("Unsupported or undefined DATATYPE: " + datatype);
        }
    }

    private static void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        currentDate = sdf.format(Calendar.getInstance().getTime());
        System.out.println("Date updated: " + currentDate);
    }

    private static void scheduleMidnightUpdate() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Calendar now = Calendar.getInstance();
        Calendar midnight = (Calendar) now.clone();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        if (midnight.before(now)) {
            midnight.add(Calendar.DAY_OF_MONTH, 1);
        }

        long initialDelay = midnight.getTimeInMillis() - now.getTimeInMillis();
        long period = TimeUnit.DAYS.toMillis(1);

        scheduler.scheduleAtFixedRate(Main::updateDate, initialDelay, period, TimeUnit.MILLISECONDS);
    }


    private static void processAllDocumentsInDatalake(String rootFolderPath, Consumer<DocumentProcessors> action) {
        try {
            Files.walk(Paths.get(rootFolderPath))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        String filePath = path.toAbsolutePath().toString();
                        System.out.println("Processing file: " + filePath);

                        Map<String, String> documentContent = readBook(filePath);

                        if (documentContent.containsKey("error")) {
                            System.out.println("Error reading file: " + documentContent.get("error"));
                            return;
                        }

                        Map<String, Map<String, List<Integer>>> invertedIndex =
                                process_inverted_index.createInvertedIndex(documentContent, "indexer\\src\\main\\resources\\stopwords.txt");
                        Map<String, Map<String, String>> metadata =
                                process_metadata.createMetadata(documentContent);

                        DocumentProcessors processors = new DocumentProcessors(invertedIndex, metadata);
                        action.accept(processors);

                        System.out.println("Processing and storage completed for file: " + filePath);
                    });
        } catch (IOException e) {
            System.out.println("Error traversing the datalake directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listener(String folderPath_1, StoreInterface_II invertedIndexInterface, StoreInterface_MD metadataInterface) {
        String folderPath = folderPath_1 + currentDate;

        while (!new File(folderPath).exists()) {
            System.out.println("Folder " + folderPath + " doesn't exist yet. Waiting for it...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Monitoring " + folderPath + " for new files...");
        Path path = Paths.get(folderPath);

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    String filePath = folderPath + "/" + fileName;
                    System.out.println("New file detected: " + filePath);
                    Thread.sleep(500);
                    Map<String, String> bookContent = readBook(filePath);
                    Map<String, Map<String, String>> metadata = process_metadata.createMetadata(bookContent);
                    Map<String, Map<String, List<Integer>>> inverted_index = process_inverted_index.createInvertedIndex(bookContent, "indexer\\src\\main\\resources\\stopwords.txt");

                    invertedIndexInterface.storeInvertedIndex(inverted_index);
                    metadataInterface.storeMetadata(metadata);
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processFile(String filePath, Consumer<DocumentProcessors> action) {
        Map<String, String> documentContent = readBook(filePath);

        if (documentContent.containsKey("error")) {
            System.out.println("Error reading file: " + documentContent.get("error"));
            return;
        }

        Map<String, Map<String, List<Integer>>> invertedIndex =
                process_inverted_index.createInvertedIndex(documentContent, "indexer\\src\\main\\resources\\stopwords.txt");
        Map<String, Map<String, String>> metadata =
                process_metadata.createMetadata(documentContent);

        DocumentProcessors processors = new DocumentProcessors(invertedIndex, metadata);
        action.accept(processors);

        System.out.println("Processing and storage completed for file: " + filePath);
    }

    private static Map<String, String> readBook(String fileName) {
        Map<String, String> result = new HashMap<>();
        StringBuilder content = new StringBuilder();

        File file = new File(fileName);
        if (!file.exists() || !file.canRead()) {
            System.out.println("File does not exist or cannot be read: " + fileName);
            result.put("error", "File does not exist or cannot be read.");
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            result.put(fileName, content.toString());
        } catch (IOException e) {
            e.printStackTrace();
            result.put("error", "Error reading file: " + e.getMessage());
        }

        return result;
    }
}

class DocumentProcessors {
    private final Map<String, Map<String, List<Integer>>> invertedIndex;
    private final Map<String, Map<String, String>> metadata;

    public DocumentProcessors(Map<String, Map<String, List<Integer>>> invertedIndex,
                              Map<String, Map<String, String>> metadata) {
        this.invertedIndex = invertedIndex;
        this.metadata = metadata;
    }

    public void storeInvertedIndex(Consumer<Map<String, Map<String, List<Integer>>>> storeFunction) {
        storeFunction.accept(invertedIndex);
    }

    public void storeMetadata(Consumer<Map<String, Map<String, String>>> storeFunction) {
        storeFunction.accept(metadata);
    }
}
