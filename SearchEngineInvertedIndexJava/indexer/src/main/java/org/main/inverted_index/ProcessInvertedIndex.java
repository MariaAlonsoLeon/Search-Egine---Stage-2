package org.main.inverted_index;

import java.io.*;
import java.util.*;

public class ProcessInvertedIndex {

    // Variable estática que contendrá las stopwords
    private static Set<String> STOPWORDS = new HashSet<>();

    // Método para cargar las stopwords desde un archivo
    private static void loadStopwords(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                STOPWORDS.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de stopwords: " + e.getMessage());
        }
    }

    public static Map<String, Map<String, List<Integer>>> createInvertedIndex(Map<String, String> book, String stopwordsFilePath) {
        // Cargar las stopwords desde el archivo especificado
        loadStopwords(stopwordsFilePath);

        Map<String, Map<String, List<Integer>>> invertedIndex = new HashMap<>();

        for (Map.Entry<String, String> entry : book.entrySet()) {
            String fullPath = entry.getKey();
            String documentName = normalizePath(fullPath);
            String content = entry.getValue();
            String[] words = content.split("\\W+");

            for (int i = 0; i < words.length; i++) {
                String word = words[i].toLowerCase();

                // Ignorar las stopwords
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

    public static void main(String[] args) {
        // Puedes llamar al método 'createInvertedIndex' pasando el libro (como Map) y la ruta del archivo de stopwords
        // Ejemplo de uso:
        Map<String, String> books = new HashMap<>();
        books.put("path/to/book1.txt", "This is a sample content with some stopwords like and, the, or.");
        books.put("path/to/book2.txt", "Another book with different words, avoiding stopwords.");

        String stopwordsFilePath = "path/to/stopwords.txt";  // Asegúrate de poner la ruta correcta
        Map<String, Map<String, List<Integer>>> index = createInvertedIndex(books, stopwordsFilePath);

        System.out.println(index);
    }
}
