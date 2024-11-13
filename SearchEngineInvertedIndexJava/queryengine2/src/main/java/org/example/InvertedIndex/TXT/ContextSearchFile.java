package org.example.InvertedIndex.TXT;

import org.example.InvertedIndex.SearchCommand;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContextSearchFile implements SearchCommand {
    private final List<String> words;
    private final String filePath;

    public ContextSearchFile(List<String> words, String filePath) {
        // Convertir todas las palabras a minúsculas para facilitar la búsqueda
        this.words = new ArrayList<>();
        for (String word : words) {
            this.words.add(word.toLowerCase());
        }
        this.filePath = filePath;
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> results = new HashMap<>();
        Map<String, Map<String, List<Integer>>> allWordPositions = new HashMap<>();

        // Leer el archivo y encontrar posiciones para cada palabra
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Leer cada línea del archivo y buscar cada palabra
            while ((line = reader.readLine()) != null) {
                for (String word : words) {
                    Pattern pattern = Pattern.compile("^\\{" + word + ": \\{(.*)}}$");
                    Matcher matcher = pattern.matcher(line.trim());

                    if (matcher.find()) {
                        String booksData = matcher.group(1);
                        Map<String, List<Integer>> bookMap = parseBookData(booksData);
                        allWordPositions.put(word, bookMap);
                    }
                }
            }

            // Combinar los resultados: buscar libros donde todas las palabras están presentes
            for (String book : getCommonBooks(allWordPositions)) {
                List<String> contextPositions = new ArrayList<>();

                // Agregar todas las posiciones de cada palabra en el mismo libro
                for (String word : words) {
                    List<Integer> positions = allWordPositions.get(word).get(book);
                    if (positions != null) {
                        contextPositions.add(word + ": " + positions.toString());
                    }
                }
                results.put(book, contextPositions);
            }

        } catch (IOException e) {
            System.err.println("Error reading the inverted index file: " + e.getMessage());
        }

        return results;
    }

    // Obtiene una lista de libros comunes donde todas las palabras están presentes
    private Set<String> getCommonBooks(Map<String, Map<String, List<Integer>>> allWordPositions) {
        Set<String> commonBooks = null;

        for (Map<String, List<Integer>> bookMap : allWordPositions.values()) {
            if (commonBooks == null) {
                commonBooks = new HashSet<>(bookMap.keySet());
            } else {
                commonBooks.retainAll(bookMap.keySet());
            }
        }
        return commonBooks != null ? commonBooks : Collections.emptySet();
    }

    // Función auxiliar para parsear los datos de libros y posiciones desde el archivo
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