package org.example.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class GutenbergDocumentDownloader implements DocumentDownloader {
    private static final String URL_PROJECT_GUTENBERG = "https://www.gutenberg.org/cache/epub/{id}/pg{id}.txt";

    @Override
    public Document downloadDocument(int bookId) throws IOException {
        String bookUrl = URL_PROJECT_GUTENBERG.replace("{id}", String.valueOf(bookId));
        System.out.println("Attempting to download document from: " + bookUrl);
        return Jsoup.connect(bookUrl)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();    }
}
