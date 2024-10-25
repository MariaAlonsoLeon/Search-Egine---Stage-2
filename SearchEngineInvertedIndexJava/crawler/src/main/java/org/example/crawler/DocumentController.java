package org.example.crawler;

import org.jsoup.nodes.Document;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DocumentController {
    private final DocumentDownloader downloader;
    private final DocumentStore documentStore;
    private final int nDocuments;
    private int bookIdCounter = 1;

    public DocumentController(DocumentDownloader downloader, DocumentStore documentStore, int nDocuments) {
        this.downloader = downloader;
        this.documentStore = documentStore;
        this.nDocuments = nDocuments;
    }

    public void startCrawler() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::scheduleDownloads, 0, 1, TimeUnit.MINUTES);
    }

    private void scheduleDownloads() {
        if (bookIdCounter <= nDocuments) {
            processDocument(getNextBookId());
        } else {
            System.exit(0);
        }
    }

    private void processDocument(int bookId) {
        try {
            Document document = downloader.downloadDocument(bookId);
            documentStore.saveDocument(document, bookId);
            System.out.println("Successfully saved document with ID: " + bookId);
        } catch (Exception e) {
            System.err.println("Error processing document: " + e.getMessage());
        }
    }

    private int getNextBookId() {
        return bookIdCounter++;
    }
}
