package org.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.main.inverted_index.*;
import org.main.metadata.*;

public class Main {
    private static final String DOCUMENT_REPOSITORY = "datalake/";
    private static final String INVERTED_INDEX_REPOSITORY = "datalake_inverted_index";
    private static final ProcessMetadata process_metadata = new ProcessMetadata();
    private static final ProcessInvertedIndex process_inverted_index = new ProcessInvertedIndex();

    private static final StoreBinaryFile_II storeBinaryFileII = new StoreBinaryFile_II();
    private static final StoreMongoDB_II store_mongoDB_II = new StoreMongoDB_II();
    private static final StoreNeo4j_II storeNeo4jII = new StoreNeo4j_II();
    private static final StoreBinaryFile_MD storeBinaryFileMD = new StoreBinaryFile_MD();
    private static final StoreMongoDB_MD store_mongoDB_MD = new StoreMongoDB_MD();
    private static final StoreNeo4j_MD storeNeo4jMD = new StoreNeo4j_MD();

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);
        String folderPath = String.format("%s/%s", DOCUMENT_REPOSITORY, formattedDate);

        listener(folderPath, storeBinaryFileII, storeBinaryFileMD);
        //Map<String, String> book1 = readBook("datalake/20241111/book_3.txt");
        //testStorage(book1);
    }

    private static void testStorage(Map<String, String> book) {
        Map<String, Map<String, String>> metadata = process_metadata.createMetadata(book);
        Map<String, Map<String, List<Integer>>> inverted_index = process_inverted_index.createInvertedIndex(book);

        storeBinaryFileII.storeInvertedIndex(inverted_index);
        //storeBinaryFileMD.storeMetadata(metadata);
    }

    private static Map<String, String> readBook(String fileName) {
        Map<String, String> result = new HashMap<>();
        StringBuilder content = new StringBuilder();
        //String line;

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

    private static void listener(String folderPath, StoreInterface_II invertedIndexInterface, StoreInterface_MD metadataInterface) {
        // Waits until the folder is created
        while (!new File(folderPath).exists()) {
            System.out.println("Folder " + folderPath + " doesn't exist yet. Waiting for it...");
            try {
                Thread.sleep(2000); // Wait for 2 seconds
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
                    key = watchService.take(); // Wait for a key to be available
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue; // Overflow event, skip
                    }

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    // Process the new file

                    String filePath = folderPath + "/" + fileName;
                    System.out.println("New file detected: " + filePath);
                    Thread.sleep(500);
                    Map<String, String> bookContent = readBook(filePath);
                    Map<String, Map<String, String>> metadata = process_metadata.createMetadata(bookContent);
                    Map<String, Map<String, List<Integer>>> inverted_index = process_inverted_index.createInvertedIndex(bookContent);

                    invertedIndexInterface.storeInvertedIndex(inverted_index);
                    metadataInterface.storeMetadata(metadata);
                }

                // Reset the key
                boolean valid = key.reset();
                if (!valid) {
                    break; // Exit loop if the key is no longer valid
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
