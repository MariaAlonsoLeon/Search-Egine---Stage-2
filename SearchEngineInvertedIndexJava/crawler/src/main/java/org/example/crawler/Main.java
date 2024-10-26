package org.example.crawler;

public class Main {
    // Crawler downloads specified number of books from Gutenberg to Document Repository
    // Then finishes working

    // the number of books we want to download:
    private static final Integer NUMBER_BOOKS = 5;

    public static void main(String[] args) {
        System.out.println("Crawler is starting 123...");

        DocumentDownloader downloader = new GutenbergDocumentDownloader();
        DocumentStore documentStore = new FileDocumentStore();
        DocumentController controller = new DocumentController(downloader, documentStore, NUMBER_BOOKS);

        controller.startCrawler();
    }
}
