package org.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.main.inverted_index.StoreBinaryFile_II;
import org.main.inverted_index.StoreMongoDB_II;
import org.main.metadata.StoreBinaryFile_MD;
import org.main.metadata.StoreMongoDB_MD;
import org.main.metadata.ProcessMetadata;
import org.main.inverted_index.ProcessInvertedIndex;

public class Main {
    private static final String DOCUMENT_REPOSITORY = "datalake_document_repository/books";
    private static final String INVERTED_INDEX_REPOSITORY = "datalake_inverted_index";
    private static final ProcessMetadata process_metadata = new ProcessMetadata();

    private static final ProcessInvertedIndex process_inverted_index = new ProcessInvertedIndex();
    private static final StoreMongoDB_II store_mongoDB_II = new StoreMongoDB_II();

    private static final StoreMongoDB_MD store_mongoDB_MD = new StoreMongoDB_MD();
    private static final StoreBinaryFile_II storeBinaryFileII = new StoreBinaryFile_II();
    private static final StoreBinaryFile_MD storeBinaryFileMD = new StoreBinaryFile_MD();

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.now();
        String folderPath = String.format("%s/%s", DOCUMENT_REPOSITORY, currentDate);

        // Start the listener for the specified folder
        listener(folderPath);

        /*Map<String, String> book = new HashMap<>();
        book.put("libro", "book main book dict space\n" +
                "plot gamma book space\n" +
                "Author: cervantes\n" +
                "Release date: 01010101\n" +
                "pepe Author: miguel\n" +
                "Language: English\n" +
                "pepito\n");
        ProcessMetadata metadata = new ProcessMetadata();
        ProcessInvertedIndex invertedIndex = new ProcessInvertedIndex();
        System.out.println(metadata.createMetadata(book));
        System.out.println(invertedIndex.createInvertedIndex(book));*/

    }

    private static Map<String, String> readBook(String fileName) {
        Map<String, String> result = new HashMap<>();
        StringBuilder content = new StringBuilder();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            result.put("fileName", fileName);
            result.put("content", content.toString());
        } catch (IOException e) {
            e.printStackTrace();
            result.put("error", "Error reading file: " + e.getMessage());
        }

        return result;
    }

    private static void listener(String folderPath) {
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
                    Map<String, String> bookContent = readBook(filePath);
                    Map<String, Map<String, String>> metadata = process_metadata.createMetadata(bookContent);
                    Map<String, Map<String, List<Integer>>> inverted_index = process_inverted_index.createInvertedIndex(bookContent);

                    /*
                    store_mongoDB_II.storeInvertedIndex(inverted_index);
                    storeBinaryFileII.storeInvertedIndex(inverted_index);

                    store_mongoDB_MD.storeMetadata(metadata);
                    storeBinaryFileMD.storeMetadata(metadata);
                    */


                }

                // Reset the key
                boolean valid = key.reset();
                if (!valid) {
                    break; // Exit loop if the key is no longer valid
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
