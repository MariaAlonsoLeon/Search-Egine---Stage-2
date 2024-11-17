package org.example.crawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.HttpStatusException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class GutenbergDocumentDownloader implements DocumentDownloader {
    private static final String URL_PROJECT_GUTENBERG = "https://www.gutenberg.org/cache/epub/%d/pg%d.txt";

    @Override
    public Document downloadDocument(int bookId) throws IOException {
        String bookUrl = String.format(URL_PROJECT_GUTENBERG, bookId, bookId);
        System.out.println("Downloading from: " + bookUrl);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(bookUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String documentText = response.body();
                return Jsoup.parse(documentText); 
            } else {
                throw new HttpStatusException("Error downloading", response.statusCode(), bookUrl);
            }
        } catch (InterruptedException e) {
            System.err.println("Request interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new IOException("Error downloading", e);
        }
    }
}
