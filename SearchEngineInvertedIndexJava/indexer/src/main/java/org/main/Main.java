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
    private static final ProcessMetadata process_metadata = new ProcessMetadata();
    private static final ProcessInvertedIndex process_inverted_index = new ProcessInvertedIndex();
    private static final StoreText_II storeTextII = new StoreText_II();
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

        // Create inverted indexes and metadata for all files in the folder
        processAllDocumentsInFolder(folderPath);
    }

    private static void processAllDocumentsInFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Folder does not exist: " + folderPath);
            return;
        }

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                String filePath = file.getAbsolutePath();
                System.out.println("Processing file: " + filePath);

                Map<String, String> documentContent = readBook(filePath);

                if (documentContent.containsKey("error")) {
                    System.out.println("Error reading file: " + documentContent.get("error"));
                    continue;
                }

                // Create and store inverted indexes in all storage types
                Map<String, Map<String, List<Integer>>> inverted_index = process_inverted_index.createInvertedIndex(documentContent);
                storeTextII.storeInvertedIndex(inverted_index);
                storeBinaryFileII.storeInvertedIndex(inverted_index);
                store_mongoDB_II.storeInvertedIndex(inverted_index);
                storeNeo4jII.storeInvertedIndex(inverted_index);

                // Create and store metadata in all storage types
                Map<String, Map<String, String>> metadata = process_metadata.createMetadata(documentContent);
                storeBinaryFileMD.storeMetadata(metadata);
                store_mongoDB_MD.storeMetadata(metadata);
                storeNeo4jMD.storeMetadata(metadata);

                System.out.println("Processing and storage completed for file: " + filePath);
            }
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