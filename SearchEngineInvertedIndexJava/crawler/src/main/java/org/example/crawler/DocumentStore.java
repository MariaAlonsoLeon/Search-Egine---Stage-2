package org.example.crawler;

import java.io.IOException;

public interface DocumentStore {
    void saveDocument(String document, String composedId) throws IOException;
}
