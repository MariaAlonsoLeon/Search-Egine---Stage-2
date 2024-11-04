package org.main.metadata;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessMetadata {

    public static Map<String, Map<String, String>> createMetadata(Map<String, String> book) {
        Map<String, Map<String, String>> metadataIndex = new HashMap<>();

        Pattern authorPattern = Pattern.compile("^Author:\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Pattern releaseDatePattern = Pattern.compile("^Release date:\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Pattern languagePattern = Pattern.compile("^Language:\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        for (Map.Entry<String, String> entry : book.entrySet()) {
            String key = entry.getKey();
            String text = entry.getValue();

            Matcher authorMatcher = authorPattern.matcher(text);
            Matcher releaseDateMatcher = releaseDatePattern.matcher(text);
            Matcher languageMatcher = languagePattern.matcher(text);

            Map<String, String> metaData = new HashMap<>();
            metaData.put("author", authorMatcher.find() ? authorMatcher.group(1) : "Not found");
            metaData.put("date", releaseDateMatcher.find() ? releaseDateMatcher.group(1) : "Not found");
            metaData.put("language", languageMatcher.find() ? languageMatcher.group(1) : "Not found");

            metadataIndex.put(key, metaData);
        }

        return metadataIndex;
    }
}