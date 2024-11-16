package org.example.Metadata.Binary;

import org.example.Metadata.SearchMetadataCommand;

import java.io.*;
import java.util.*;

public class SearchByDateBinaryFile implements SearchMetadataCommand {
    private final String date;
    private final String filePath;

    // Mapa de claves para asociar abreviaturas de campos (igual que en StoreBinary_MD)
    private static final Map<Byte, String> metadataKeys = new HashMap<>();
    static {
        metadataKeys.put((byte) 1, "documentName");
        metadataKeys.put((byte) 2, "author");
        metadataKeys.put((byte) 3, "date");
        metadataKeys.put((byte) 4, "language");
    }

    public SearchByDateBinaryFile(String date, String filePath) {
        this.date = date;
        this.filePath = filePath;
    }

    @Override
    public List<String> execute() {
        List<String> documentNames = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            String currentDocumentName = null;
            String currentDate = null;

            // Leer el archivo binario
            while (dis.available() > 0) {
                byte key = dis.readByte();

                if (key == 0) {
                    // Fin de registro, verificar si la fecha coincide
                    if (currentDate != null && currentDate.equals(date) && currentDocumentName != null) {
                        documentNames.add(currentDocumentName);
                    }
                    // Reiniciar para el siguiente registro
                    currentDocumentName = null;
                    currentDate = null;
                } else if (metadataKeys.containsKey(key)) {
                    String value = dis.readUTF();

                    // Actualizar valores segÃºn clave
                    if (key == 1) {
                        currentDocumentName = value;
                    } else if (key == 3) {
                        currentDate = value;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the binary metadata file: " + e.getMessage());
        }

        return documentNames;
    }
}