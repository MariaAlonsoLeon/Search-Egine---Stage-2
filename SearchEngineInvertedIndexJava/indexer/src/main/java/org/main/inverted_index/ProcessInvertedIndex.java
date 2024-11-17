package org.main.inverted_index;
import java.util.*;

public class ProcessInvertedIndex {

    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any",
            "are", "aren't", "aren't", "as", "at", "be", "because", "been", "before", "being",
            "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "couldnt",
            "did", "didn't", "does", "doesn't", "don't", "for", "from", "had", "hadn't", "has",
            "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here",
            "here's", "hers", "herself", "hereafter", "hereby", "herein", "hereupon", "herself",
            "how", "how's", "howsoever", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "is",
            "isn't", "isn't", "it", "it's", "it'd", "it'll", "it's", "it's", "itself", "let",
            "me", "me", "mine", "my", "myself", "no", "nor", "not", "of", "off", "on", "once",
            "only", "or", "other", "others", "ought", "our", "ours", "ourselves", "ourselves",
            "over", "own", "same", "than", "that", "that's", "that'd", "that'll", "that’s", "that",
            "these", "they", "they'd", "they'll", "they're", "they've", "this", "this’s", "those",
            "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd",
            "we'll", "we're", "we've", "what", "what's", "whatsoever", "which", "which's", "while",
            "who", "who's", "whoever", "whom", "whom's", "whose", "why", "why's", "whyever",
            "with", "won't", "would", "wouldn't"));

    public static Map<String, Map<String, List<Integer>>> createInvertedIndex(Map<String, String> book) {
        Map<String, Map<String, List<Integer>>> invertedIndex = new HashMap<>();

        for (Map.Entry<String, String> entry : book.entrySet()) {
            String fullPath = entry.getKey();
            String documentName = normalizePath(fullPath);
            String content = entry.getValue();
            String[] words = content.split("\\W+");

            for (int i = 0; i < words.length; i++) {
                String word = words[i].toLowerCase();

                if (!word.isEmpty() && !STOPWORDS.contains(word)) {
                    invertedIndex
                            .computeIfAbsent(word, k -> new HashMap<>())
                            .computeIfAbsent(documentName, k -> new ArrayList<>())
                            .add(i);
                }
            }
        }
        return invertedIndex;
    }

    private static String normalizePath(String filePath) {
        String[] pathParts = filePath.split("[/\\\\]");
        int length = pathParts.length;

        if (length >= 3) {
            String folderName = pathParts[length - 2];
            String fileName = pathParts[length - 1];

            String fileNameWithoutExtension = fileName.replaceFirst("\\.txt$", "");
            return folderName + "/" + fileNameWithoutExtension;
        }

        return filePath;
    }
}
