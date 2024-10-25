package org.example.crawler;

import org.jsoup.nodes.Document;
import java.io.IOException;

public interface DocumentStore {
    void saveDocument(Document document, int bookId) throws IOException;
}
