package org.example.crawler;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) {
        GutenbergDocumentDownloader downloader = new GutenbergDocumentDownloader();

        int nDocuments = 5;

        FileDocumentStore documentStore = new FileDocumentStore();

        DocumentController controller = new DocumentController(downloader, documentStore, nDocuments);

        controller.startCrawler();

        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("Document Crawler started. Press Ctrl+C to stop.");

        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Process interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}
