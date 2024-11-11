package org.example.crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class FileDocumentStore implements DocumentStore {
    private static final String DOCUMENT_REPOSITORY_PATH = "datalake";

    @Override
    public void saveDocument(String document, String composedId) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }

        LocalDateTime documentDate = extractDateFromId(composedId);
        createFolderIfNotExists(documentDate);
        createFile(document, documentDate);
    }

    private void createFile(String document, LocalDateTime documentDate) {
        String folderPath = generateFolderPath(documentDate);
        int bookNumber = getNextBookNumber(folderPath);
        String fileName = "book_" + bookNumber + ".txt";
        String filePath = folderPath + File.separator + fileName;

        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(document);
            System.out.println("Document saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            throw new RuntimeException("File writing failed", e);
        }
    }

    private void createFolderIfNotExists(LocalDateTime documentDate) {
        String folderPath = generateFolderPath(documentDate);
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Directory created: " + folderPath);
            } else {
                System.err.println("Failed to create directory: " + folderPath);
                throw new RuntimeException("Directory creation failed");
            }
        }
    }

    private String generateFolderPath(LocalDateTime documentDate) {
        String folderDate = documentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return DOCUMENT_REPOSITORY_PATH + File.separator + folderDate;
    }

    private LocalDateTime extractDateFromId(String composedId) {
        String dateStr = composedId.split("/")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        return LocalDateTime.parse(dateStr, formatter);
    }

    private int getNextBookNumber(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return 1;
        }

        File[] existingBooks = folder.listFiles((dir, name) -> name.startsWith("book_") && name.endsWith(".txt"));
        if (existingBooks == null || existingBooks.length == 0) {
            return 1;
        }

        return Arrays.stream(existingBooks)
                .map(file -> {
                    String fileName = file.getName();
                    return Integer.parseInt(fileName.substring(5, fileName.lastIndexOf('.')));
                })
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
    }
}

