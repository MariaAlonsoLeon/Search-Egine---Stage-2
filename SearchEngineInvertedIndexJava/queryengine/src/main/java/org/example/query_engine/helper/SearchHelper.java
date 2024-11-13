package org.example.query_engine.helper;

import java.util.List;

public interface SearchHelper {
    String searchWord(String word);
    String searchAnd(String word1, String word2);
    String searchOr(String word1, String word2);
    String searchNot(String word);
    String paginate(String word, int pageSize, int pageNumber);
}
