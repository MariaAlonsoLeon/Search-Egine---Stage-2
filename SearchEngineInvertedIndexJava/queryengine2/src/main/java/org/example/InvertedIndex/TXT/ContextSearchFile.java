package org.example.InvertedIndex.TXT;

import org.example.InvertedIndex.SearchCommand;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContextSearchFile implements SearchCommand {
    private final String word;
    private final int contextSize;
    private final String filePath;
    private final String documentFolderPath;

    public ContextSearchFile(String word, int contextSize, String filePath, String documentFolderPath) {
        this.word = word.toLowerCase();
        this.contextSize = contextSize;
        this.filePath = filePath;
        this.documentFolderPath = documentFolderPath;
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> contexts = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile("^\\{" + word + ": \\{(.*)}}$");

            // Leer cada línea para buscar la palabra y extraer la información
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line.trim());
                if (matcher.find()) {
                    String booksData = matcher.group(1);
                    Map<String, List<Integer>> bookMap = parseBookData(booksData);

                    // Procesar cada libro que contiene la palabra
                    for (Map.Entry<String, List<Integer>> entry : bookMap.entrySet()) {
                        String bookTitle = entry.getKey();
                        List<Integer> positions = entry.getValue();
                        List<String> bookContexts = new ArrayList<>();

                        // Leer el archivo de texto del libro para obtener el contenido
                        File bookFile = new File(documentFolderPath, bookTitle + ".txt");
                        StringBuilder bookContent = new StringBuilder();

                        try (BufferedReader bookReader = new BufferedReader(new FileReader(bookFile))) {
                            String lineContent;
                            while ((lineContent = bookReader.readLine()) != null) {
                                bookContent.append(lineContent).append(" ");
                            }
                        }

                        // Convertir el contenido a minúsculas y dividir en palabras
                        String bookText = bookContent.toString().toLowerCase();
                        String[] words = bookText.split("\\W+");

                        // Generar contexto alrededor de cada posición
                        for (Integer pos : positions) {
                            int start = Math.max(0, pos - contextSize);
                            int end = Math.min(words.length, pos + contextSize);
                            String context = String.join(" ", Arrays.asList(words).subList(start, end));
                            bookContexts.add(context);
                        }

                        contexts.put(bookTitle, bookContexts);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the index file or document files: " + e.getMessage());
        }

        return contexts;
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
