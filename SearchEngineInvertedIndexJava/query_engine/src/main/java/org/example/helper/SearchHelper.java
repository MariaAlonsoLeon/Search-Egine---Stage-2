package org.example.helper;

import java.util.List;
import java.util.Map;

public interface SearchHelper {
    Map<String, List<String>> searchSingleWordII(String word);
    Map<String, List<String>> searchWithContextII(String word, int contextSize, String documentFolderPath);
    List<String> searchByAuthorMD(String author);
    List<String> searchByDateMD(String date);
    List<String> searchByLanguageMD(String language);
    Map<String, String> fetchMD(String bookName);
}
