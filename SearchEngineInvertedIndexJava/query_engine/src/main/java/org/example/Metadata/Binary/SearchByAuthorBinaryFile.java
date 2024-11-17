package org.example.Metadata.Binary;

import org.example.Metadata.SearchMetadataCommand;

import java.io.*;
import java.util.*;

public class SearchByAuthorBinaryFile implements SearchMetadataCommand {
    private final String author;
    private final String filePath;

    // Mapa para asociar abreviaturas de campos (debe coincidir con el que se usa en StoreBinary_MD)
    private static final Map<Byte, String> metadataKeys = new HashMap<>();
    static {
        metadataKeys.put((byte) 1, "documentName");
        metadataKeys.put((byte) 2, "author");
        metadataKeys.put((byte) 3, "date");
        metadataKeys.put((byte) 4, "language");
    }

    public SearchByAuthorBinaryFile(String author, String filePath) {
        this.author = author.toLowerCase();
        this.filePath = filePath;
    }

    @Override
    public List<String> execute() {
        List<String> documentNames = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            String currentDocumentName = null;
            String currentAuthor = null;
            while (dis.available() > 0) {
                byte key = dis.readByte();

                if (key == 0) {
                    // Fin de registro, verificar si coincide con el autor buscado
                    if (currentAuthor != null && currentAuthor.equals(author) && currentDocumentName != null) {
                        documentNames.add(currentDocumentName);
                    }
                    currentDocumentName = null;
                    currentAuthor = null;
                } else if (metadataKeys.containsKey(key)) {
                    String value = dis.readUTF();

                    // Actualizar valores según clave
                    if (key == 1) {
                        currentDocumentName = value;
                    } else if (key == 2) {
                        currentAuthor = value.toLowerCase();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the binary metadata file: " + e.getMessage());
        }

        return documentNames;
    }
}