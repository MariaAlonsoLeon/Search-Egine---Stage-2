package org.main.metadata;

import java.io.*;
import java.util.Map;

public class StoreText_MD implements StoreInterface_MD {
    private final String filePath = "datamart/metadata.txt";

    @Override
    public void storeMetadata(Map<String, Map<String, String>> metadata) {
        File file = new File(filePath);

        // Verificar que el directorio padre exista
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Escribir los metadatos al final del archivo en formato JSON-like
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) { // true para modo "append"
            for (Map.Entry<String, Map<String, String>> documentEntry : metadata.entrySet()) {
                String bookName = documentEntry.getKey();
                Map<String, String> metadataBody = documentEntry.getValue();

                // Construir la cadena JSON-like
                StringBuilder jsonLine = new StringBuilder();
                jsonLine.append("{\"documentName\": \"").append(bookName).append("\"");

                for (Map.Entry<String, String> entry : metadataBody.entrySet()) {
                    jsonLine.append(", \"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
                }
                jsonLine.append("}");

                // Escribir la línea al final del archivo
                writer.write(jsonLine.toString());
                writer.newLine();
            }
            System.out.println("Metadata stored successfully in file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing metadata to file: " + e.getMessage());
        }
    }
}