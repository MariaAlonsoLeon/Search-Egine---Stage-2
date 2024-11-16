package org.example.helper;

import java.util.Collections;
import java.util.List;

public class MongoDbSearchHelper implements SearchHelper {
    @Override
    public List<String> searchSingleWordII(String word) {
        return Collections.emptyList();
    }

    @Override
    public List<String> searchWithContextII(String word, int contextSize, String documentFolderPath) {
        return Collections.emptyList();
    }

    @Override
    public List<String> searchByAuthorMD(String author) {
        return Collections.emptyList();
    }

    @Override
    public List<String> searchByDateMD(String date) {
        return Collections.emptyList();
    }

    @Override
    public List<String> searchByLanguageMD(String language) {
        return Collections.emptyList();
    }

    @Override
    public List<String> fetchMD(String bookName) {
        return Collections.emptyList();
    }
}
