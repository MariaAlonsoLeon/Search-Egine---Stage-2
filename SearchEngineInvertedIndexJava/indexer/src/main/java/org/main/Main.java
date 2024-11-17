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
    private static final StoreInterface_II storeTextII = new StoreText_II();
    private static final StoreInterface_II storeBinaryFileII = new StoreBinary_II();
    private static final StoreInterface_II store_mongoDB_II = new StoreMongoDB_II();
    private static final StoreInterface_II storeNeo4jII = new StoreNeo4j_II();
    private static final StoreInterface_MD storeBinaryFileMD = new StoreBinary_MD();
    private static final StoreInterface_MD store_mongoDB_MD = new StoreMongoDB_MD();
    private static final StoreInterface_MD storeNeo4jMD = new StoreNeo4j_MD();
    private static final StoreInterface_MD storeTextMD = new StoreText_MD();

    public static void main(String[] args) {
        String datatype = System.getenv("DATATYPE");
        System.out.println("DATATYPE: " + datatype);

        updateDate();
        scheduleIndexerExecution();

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
            // Ejecución inicial del procesamiento de todos los documentos en el datalake
            processAllDocumentsInDatalake(DOCUMENT_REPOSITORY, actions.get(datatype));
        } else {
            System.out.println("Unsupported or undefined DATATYPE: " + datatype);
        }
    }

    // Método para ejecutar la tarea periódica cada 1 minuto
    private static void scheduleIndexerExecution() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                String datatype = System.getenv("DATATYPE");
                if (datatype != null) {
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

                    if (actions.containsKey(datatype)) {
                        processAllDocumentsInDatalake(DOCUMENT_REPOSITORY, actions.get(datatype));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error while executing the indexer: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private static void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        currentDate = sdf.format(Calendar.getInstance().getTime());
        System.out.println("Date updated: " + currentDate);
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
                                process_inverted_index.createInvertedIndex(documentContent);
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

    public DocumentProcessors(Map<String, Map<String, List<Integer>>> invertedIndex, Map<String, Map<String, String>> metadata) {
        this.invertedIndex = invertedIndex;
        this.metadata = metadata;
    }

    public void storeInvertedIndex(Consumer<Map<String, Map<String, List<Integer>>>> storeInvertedIndexMethod) {
        storeInvertedIndexMethod.accept(invertedIndex);
    }

    public void storeMetadata(Consumer<Map<String, Map<String, String>>> storeMetadataMethod) {
        storeMetadataMethod.accept(metadata);
    }
}
