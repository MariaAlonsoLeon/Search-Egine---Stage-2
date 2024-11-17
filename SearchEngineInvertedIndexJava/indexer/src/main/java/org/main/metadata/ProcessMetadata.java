package org.main.metadata;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessMetadata {

    public static Map<String, Map<String, String>> createMetadata(Map<String, String> book) {
        Map<String, Map<String, String>> metadataIndex = new HashMap<>();

        Pattern authorPattern = Pattern.compile("^Author:\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Pattern releaseDatePattern = Pattern.compile("^Release date:\\s*([A-Za-z]+ \\d{1,2}, \\d{4})", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Pattern languagePattern = Pattern.compile("^Language:\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        for (Map.Entry<String, String> entry : book.entrySet()) {
            String fullPath = entry.getKey();
            String documentName = normalizePath(fullPath);
            String text = entry.getValue();

            Matcher authorMatcher = authorPattern.matcher(text);
            Matcher releaseDateMatcher = releaseDatePattern.matcher(text);
            Matcher languageMatcher = languagePattern.matcher(text);

            Map<String, String> metaData = new HashMap<>();
            metaData.put("author", authorMatcher.find() ? authorMatcher.group(1) : "Not found");

            if (releaseDateMatcher.find()) {
                try {
                    String dateStr = releaseDateMatcher.group(1);
                    metaData.put("date", outputDateFormat.format(inputDateFormat.parse(dateStr)));
                } catch (ParseException e) {
                    metaData.put("date", "Invalid format");
                }
            } else {
                metaData.put("date", "Not found");
            }

            metaData.put("language", languageMatcher.find() ? languageMatcher.group(1) : "Not found");

            metadataIndex.put(documentName, metaData);
        }

        return metadataIndex;
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
