package org.example.crawler;

import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileDocumentStore implements DocumentStore {
    private static final String DOCUMENT_REPOSITORY_PATH = "datalake_document_repository/books/";

    @Override
    public void saveDocument(Document document, int bookId) throws IOException {
        System.out.println("Saving document with ID: " + bookId);
        String folderPath = generateFolderPath();
        createFolder(folderPath);
        createFile(document, folderPath, bookId);
    }

    private void createFile(Document document, String folderPath, int bookId) throws IOException {
        String filePath = folderPath + bookId + ".txt";
        System.out.println("Creating file: " + filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(document.text());
        }
        System.out.println("File created successfully: " + filePath);
    }

    private void createFolder(String folderPath) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private String generateFolderPath() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(new Date());
        return DOCUMENT_REPOSITORY_PATH + currentDate + "/";
    }
}
