package org.example.Metadata.TXT;

import org.example.Metadata.SearchMetadataCommand;

import java.io.*;
import java.util.*;

public class SearchByAuthorFile implements SearchMetadataCommand {
    private final String author;
    private final String filePath;

    public SearchByAuthorFile(String author, String filePath) {
        this.author = author.toLowerCase(); // Convertir a minúsculas para una búsqueda insensible a mayúsculas
        this.filePath = filePath;
    }

    @Override
    public List<String> execute() {
        List<String> documentNames = new ArrayList<>();

        // Leer el archivo y buscar todos los documentos que tengan el autor especificado
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Leer cada línea del archivo que contiene los metadatos
            while ((line = reader.readLine()) != null) {
                // Buscar si la línea contiene el autor especificado
                if (line.toLowerCase().contains("\"author\": \"" + author + "\"")) {
                    // Extraer el nombre del documento (libro) de los metadatos
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
        // Buscar el patrón para el nombre del documento en la línea de metadatos
        String nameKey = "\"documentName\": \"";
        int nameStart = jsonLine.indexOf(nameKey);

        if (nameStart != -1) {
            nameStart += nameKey.length();
            int nameEnd = jsonLine.indexOf("\"", nameStart);
            if (nameEnd != -1) {
                return jsonLine.substring(nameStart, nameEnd);
            }
        }
        return null; // Si no se encuentra el nombre del documento, retornar null
    }
}