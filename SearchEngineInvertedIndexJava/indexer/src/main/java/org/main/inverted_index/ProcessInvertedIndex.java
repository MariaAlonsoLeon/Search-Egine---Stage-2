package org.main.inverted_index;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessInvertedIndex {

    public static Map<String, Map<String, List<Integer>>> createInvertedIndex(Map<String, String> book) {
        Map<String, Map<String, List<Integer>>> documentInvertedIndex = new HashMap<>();

        System.out.println(book);

        for (Map.Entry<String, String> entry : book.entrySet()) {
            String documentName = entry.getKey();
            System.out.println(documentName);
            String content = entry.getValue();
            String[] words = content.split("\\W+");

            Map<String, List<Integer>> invertedIndex = new HashMap<>();
            for (int i = 0; i < words.length; i++) {
                String word = words[i].toLowerCase();
                invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(i);
            }

            documentInvertedIndex.put(documentName, invertedIndex);
        }
        return documentInvertedIndex;
    }

}
