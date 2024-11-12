package org.main.inverted_index;

import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreText_II implements StoreInterface_II {
    private final String FILEPATH = "datamart/inverted_index.txt";

    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        File file = new File(FILEPATH);
        if (invertedIndex.isEmpty()) {
            System.out.println("The inverted index is empty. Nothing will be written.");
            return;
        }

        // Ensure parent directory exists
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Step 1: Load existing data from the file into a list of lines
        List<String> lines = new ArrayList<>();
        try {
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return;
        }

        // Step 2: Update lines with new inverted index data
        Map<String, Map<String, List<Integer>>> updatedInvertedIndex = new HashMap<>();
        for (String line : lines) {
            String cleanedLine = line.trim();
            Pattern pattern = Pattern.compile("^\\{(.*?): \\{(.*)}}$");
            Matcher matcher = pattern.matcher(cleanedLine);

            if (matcher.find()) {
                String word = matcher.group(1).trim();
                String mapStr = matcher.group(2).trim();
                Map<String, List<Integer>> bookData = parseMap(mapStr);
                updatedInvertedIndex.put(word, bookData);
            }
        }

        // Merge new data into existing data
        for (Map.Entry<String, Map<String, List<Integer>>> wordEntry : invertedIndex.entrySet()) {
            String word = wordEntry.getKey();
            Map<String, List<Integer>> newBookData = wordEntry.getValue();

            if (updatedInvertedIndex.containsKey(word)) {
                Map<String, List<Integer>> existingBookData = updatedInvertedIndex.get(word);
                for (Map.Entry<String, List<Integer>> bookEntry : newBookData.entrySet()) {
                    String book = bookEntry.getKey();
                    List<Integer> newPositions = bookEntry.getValue();

                    if (existingBookData.containsKey(book)) {
                        Set<Integer> mergedPositions = new HashSet<>(existingBookData.get(book));
                        mergedPositions.addAll(newPositions);
                        existingBookData.put(book, new ArrayList<>(mergedPositions));
                    } else {
                        existingBookData.put(book, new ArrayList<>(newPositions));
                    }
                }
            } else {
                updatedInvertedIndex.put(word, new HashMap<>(newBookData));
            }
        }

        // Step 3: Write the updated inverted index back to the file, one line per word
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Map<String, List<Integer>>> entry : updatedInvertedIndex.entrySet()) {
                String word = entry.getKey();
                Map<String, List<Integer>> bookData = entry.getValue();
                bw.write(String.format("{%s: %s}%n", word, mapToString(bookData)));
            }
            System.out.println("File written correctly: " + file);
        } catch (IOException e) {
            System.err.println("Error writing the file: " + e.getMessage());
        }
    }

    // Parses a JSON-like string into a map of books and positions
    private static Map<String, List<Integer>> parseMap(String mapStr) {
        Map<String, List<Integer>> map = new HashMap<>();
        mapStr = mapStr.replaceAll("[{}]", "");
        String[] entries = mapStr.split(", ");

        for (String entry : entries) {
            String[] parts = entry.split(": ");
            if (parts.length == 2) {
                String book = parts[0].trim();
                String[] positionsStr = parts[1].replaceAll("[\\[\\]]", "").split(", ");
                List<Integer> positions = new ArrayList<>();
                for (String pos : positionsStr) {
                    positions.add(Integer.parseInt(pos.trim()));
                }
                map.put(book, positions);
            }
        }
        return map;
    }

    // Converts a map to a JSON-like string representation for writing
    private static String mapToString(Map<String, List<Integer>> map) {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("}");
        return sb.toString();
    }
}
