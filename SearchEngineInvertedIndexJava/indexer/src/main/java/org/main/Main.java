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

    public static void main(String[] args) {
        // Obtener DATATYPE de la variable de entorno
        String datatype = System.getenv("DATATYPE");
        System.out.println("DATATYPE: " + datatype);

        updateDate();
        scheduleMidnightUpdate();

        Map<String, Consumer<DocumentProcessors>> actions = defineActions();

        if (datatype != null && actions.containsKey(datatype)) {
            processAllDocumentsInDatalake(DOCUMENT_REPOSITORY, actions.get(datatype));
            startListener(DOCUMENT_REPOSITORY, actions.get(datatype));
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

    private static Map<String, Consumer<DocumentProcessors>> defineActions() {
        Map<String, Consumer<DocumentProcessors>> actions = new HashMap<>();

        actions.put("Text", processors -> {
            processors.storeInvertedIndex(new StoreText_II()::storeInvertedIndex);
            processors.storeMetadata(new StoreText_MD()::storeMetadata);
        });

        actions.put("BinaryFile", processors -> {
            processors.storeInvertedIndex(new StoreBinary_II()::storeInvertedIndex);
            processors.storeMetadata(new StoreBinary_MD()::storeMetadata);
        });

        actions.put("MongoDB", processors -> {
            processors.storeInvertedIndex(new StoreMongoDB_II()::storeInvertedIndex);
            processors.storeMetadata(new StoreMongoDB_MD()::storeMetadata);
        });

        actions.put("NEO4J", processors -> {
            processors.storeInvertedIndex(new StoreNeo4j_II()::storeInvertedIndex);
            processors.storeMetadata(new StoreNeo4j_MD()::storeMetadata);
        });

        return actions;
    }

    private static void processAllDocumentsInDatalake(String rootFolderPath, Consumer<DocumentProcessors> action) {
        try {
            Files.walk(Paths.get(rootFolderPath))
                    .filter(Files::isRegularFile)
                    .forEach(path -> processFile(path.toAbsolutePath().toString(), action));
        } catch (IOException e) {
            System.err.println("Error traversing the datalake directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void startListener(String rootFolderPath, Consumer<DocumentProcessors> action) {
        Path path = Paths.get(rootFolderPath, currentDate);

        System.out.println("Initializing listener for: " + path);
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

                    if (kind == StandardWatchEventKinds.OVERFLOW) continue;

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filePath = path.resolve(ev.context());
                    processFile(filePath.toString(), action);
                }

                if (!key.reset()) break;
            }
        } catch (IOException e) {
            System.err.println("Error initializing the listener: " + e.getMessage());
        }
    }

    private static void processFile(String filePath, Consumer<DocumentProcessors> action) {
        Map<String, String> documentContent = readBook(filePath);

        if (documentContent.containsKey("error")) {
            System.err.println("Error reading file: " + documentContent.get("error"));
            return;
        }

        Map<String, Map<String, List<Integer>>> invertedIndex =
                process_inverted_index.createInvertedIndex(documentContent, "indexer\\src\\main\\resources\\stopwords.txt");
        Map<String, Map<String, String>> metadata =
                process_metadata.createMetadata(documentContent);

        DocumentProcessors processors = new DocumentProcessors(invertedIndex, metadata);
        action.accept(processors);

        System.out.println("Processing completed for file: " + filePath);
    }

    private static Map<String, String> readBook(String fileName) {
        Map<String, String> result = new HashMap<>();
        StringBuilder content = new StringBuilder();

        File file = new File(fileName);
        if (!file.exists() || !file.canRead()) {
            result.put("error", "File does not exist or cannot be read.");
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            result.put(fileName, content.toString());
        } catch (IOException e) {
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
