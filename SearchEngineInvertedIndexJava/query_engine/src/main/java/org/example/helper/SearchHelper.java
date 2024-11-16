package org.example.helper;

import java.util.List;

public interface SearchHelper {
    List<String> searchSingleWordII(String word);
    List<String> searchWithContextII(String word, int contextSize, String documentFolderPath);
    List<String> searchByAuthorMD(String author);
    List<String> searchByDateMD(String date);
    List<String> searchByLanguageMD(String language);
    List<String> fetchMD(String bookName);
}
