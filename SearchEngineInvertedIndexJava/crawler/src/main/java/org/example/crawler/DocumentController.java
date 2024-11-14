package org.example.crawler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DocumentController {
    private final GutenbergDocumentDownloader downloader;
    private final FileDocumentStore documentStore;
    private final ScheduledExecutorService scheduler;
    private int bookIdCounter = 1;
    private final int nDocuments;

    public DocumentController(GutenbergDocumentDownloader downloader, FileDocumentStore documentStore, int nDocuments) {
        this.downloader = downloader;
        this.documentStore = documentStore;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.nDocuments = nDocuments;
    }

    public void startCrawler() {
        scheduleDownloads();
    }

    private void scheduleDownloads() {
        Runnable task = () -> {
            for (int i = 0; i < nDocuments; i++) {
                int nextBookId = getNextBookId();
                processDocument(nextBookId);
            }
        };
        
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    private void processDocument(int bookId) {
        try {
            String document = downloader.downloadDocument(bookId).wholeText();
            LocalDateTime currentDate = LocalDateTime.now();
            
            String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));
            String composedId = formattedDate + "/" + bookId;
            
            documentStore.saveDocument(document, composedId);
            System.out.println("Successfully saved document with ID: " + composedId);
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        System.err.println("Error occurred: " + e.getMessage());
    }

    private int getNextBookId() {
        return bookIdCounter++;
    }
}
