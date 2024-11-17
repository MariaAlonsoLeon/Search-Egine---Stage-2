package org.example.InvertedIndex.TXT;

import org.example.InvertedIndex.SearchCommand;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleWordSearchFile implements SearchCommand {
    private final String word;
    private final String filePath;

    public SingleWordSearchFile(String word, String filePath) {
        this.word = word.toLowerCase();
        this.filePath = filePath;
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> results = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile("^\\{" + word + ": \\{(.*)}}$");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line.trim());
                if (matcher.find()) {
                    String booksData = matcher.group(1);
                    Map<String, List<Integer>> bookMap = parseBookData(booksData);

                    for (Map.Entry<String, List<Integer>> entry : bookMap.entrySet()) {
                        String book = entry.getKey();
                        List<Integer> positions = entry.getValue();
                        results.put(book, formatPositions(positions));
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the inverted index file: " + e.getMessage());
        }

        return results;
    }

    private List<String> formatPositions(List<Integer> positions) {
        List<String> formattedPositions = new ArrayList<>();
        for (Integer pos : positions) {
            formattedPositions.add(pos.toString());
        }
        return formattedPositions;
    }

    private Map<String, List<Integer>> parseBookData(String booksData) {
        Map<String, List<Integer>> bookMap = new HashMap<>();
        String[] bookEntries = booksData.split(", ");

        for (String entry : bookEntries) {
            String[] parts = entry.split(": ");
            if (parts.length == 2) {
                String book = parts[0].trim();
                String[] positionsStr = parts[1].replaceAll("[\\[\\]]", "").split(", ");
                List<Integer> positions = new ArrayList<>();

                for (String pos : positionsStr) {
                    positions.add(Integer.parseInt(pos.trim()));
                }
                bookMap.put(book, positions);
            }
        }
        return bookMap;
    }
}