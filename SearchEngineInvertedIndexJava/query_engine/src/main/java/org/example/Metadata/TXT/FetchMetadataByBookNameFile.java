package org.example.Metadata.TXT;

import org.example.Metadata.FetchMetadataCommand;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FetchMetadataByBookNameFile implements FetchMetadataCommand {
    private final String bookName;
    private final String filePath;

    public FetchMetadataByBookNameFile(String bookName, String filePath) {
        this.bookName = bookName.toLowerCase();
        this.filePath = filePath;
    }

    @Override
    public Map<String, String> execute() {
        Map<String, String> metadata = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Buscar si la línea contiene el nombre del libro
                if (line.contains("\"documentName\": \"" + bookName + "\"")) {
                    System.out.println("A");
                    metadata.put("author", extractField(line, "author"));
                    metadata.put("date", extractField(line, "date"));
                    metadata.put("language", extractField(line, "language"));
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the metadata file: " + e.getMessage());
        }

        return metadata;
    }

    private String extractField(String jsonLine, String fieldName) {
        // Buscar el patrón para el campo en la línea de metadatos
        String fieldKey = "\"" + fieldName + "\": \"";
        int fieldStart = jsonLine.indexOf(fieldKey);

            if (fieldStart != -1) {
            fieldStart += fieldKey.length();
            int fieldEnd = jsonLine.indexOf("\"", fieldStart);
            if (fieldEnd != -1) {
                return jsonLine.substring(fieldStart, fieldEnd);
            }
        }
        return null;
    }
}