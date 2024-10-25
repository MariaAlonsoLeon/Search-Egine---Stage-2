package org.example.crawler;

import org.jsoup.nodes.Document;
import java.io.IOException;

public interface DocumentDownloader {
    Document downloadDocument(int bookId) throws IOException;
}
