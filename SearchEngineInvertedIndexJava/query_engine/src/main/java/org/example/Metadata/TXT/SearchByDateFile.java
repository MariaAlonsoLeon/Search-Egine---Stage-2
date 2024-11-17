package org.example.Metadata.TXT;

import org.example.Metadata.SearchMetadataCommand;

import java.io.*;
import java.util.*;

public class SearchByDateFile implements SearchMetadataCommand {
    private final String date;
    private final String filePath;

    public SearchByDateFile(String date, String filePath) {
        this.date = date;
        this.filePath = filePath;
    }

    @Override
    public List<String> execute() {
        List<String> documentNames = new ArrayList<>();

        // Leer el archivo y buscar todos los documentos que tengan la fecha especificada
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Leer cada línea del archivo que contiene los metadatos
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"date\": \"" + date + "\"")) {
                    String documentName = extractDocumentName(line);
                    if (documentName != null) {
                        documentNames.add(documentName);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the metadata file: " + e.getMessage());
        }

        return documentNames;
    }

    // Método auxiliar para extraer el nombre del documento de la línea JSON-like
    private String extractDocumentName(String jsonLine) {
        String nameKey = "\"documentName\": \"";
        int nameStart = jsonLine.indexOf(nameKey);

        if (nameStart != -1) {
            nameStart += nameKey.length();
            int nameEnd = jsonLine.indexOf("\"", nameStart);
            if (nameEnd != -1) {
                return jsonLine.substring(nameStart, nameEnd);
            }
        }
        return null;
    }
}