package org.example.Metadata.TXT;

import org.example.Metadata.SearchMetadataCommand;

import java.io.*;
import java.util.*;

public class SearchByLanguageFile implements SearchMetadataCommand {
    private final String language;
    private final String filePath;

    public SearchByLanguageFile(String language, String filePath) {
        this.language = language.toLowerCase();
        this.filePath = filePath;
    }

    @Override
    public List<String> execute() {
        List<String> documentNames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("\"language\": \"" + language + "\"")) {
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