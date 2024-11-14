package org.main.inverted_index;

import java.io.*;
import java.util.*;

public class StoreBinary_II implements StoreInterface_II {
    private final String FILEPATH = "datamart/inverted_index.dat";

    // Map to encode keys for compact storage in binary
    private static final Map<String, Byte> FIELD_CODES = new HashMap<>();
    private static final Map<Byte, String> REVERSE_FIELD_CODES = new HashMap<>();

    static {
        FIELD_CODES.put("word", (byte) 1);
        FIELD_CODES.put("document", (byte) 2);
        FIELD_CODES.put("positions", (byte) 3);

        // Reverse mapping for reading data
        for (Map.Entry<String, Byte> entry : FIELD_CODES.entrySet()) {
            REVERSE_FIELD_CODES.put(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FILEPATH, true))) { // "true" para abrir en modo append
            for (Map.Entry<String, Map<String, List<Integer>>> wordEntry : invertedIndex.entrySet()) {
                String word = wordEntry.getKey();
                Map<String, List<Integer>> documents = wordEntry.getValue();

                // Escribir el campo "word"
                dos.writeByte(FIELD_CODES.get("word"));
                dos.writeUTF(word);

                for (Map.Entry<String, List<Integer>> docEntry : documents.entrySet()) {
                    String documentName = docEntry.getKey();
                    List<Integer> positions = docEntry.getValue();

                    // Escribir el nombre del documento
                    dos.writeByte(FIELD_CODES.get("document"));
                    dos.writeUTF(documentName);

                    // Escribir la lista de posiciones
                    dos.writeByte(FIELD_CODES.get("positions"));
                    dos.writeInt(positions.size()); // Número de posiciones
                    for (int pos : positions) {
                        dos.writeInt(pos); // Escribir cada posición
                    }
                }
                dos.writeByte(0); // Fin de un registro de palabra
            }
            System.out.println("Inverted index stored successfully in binary file: " + FILEPATH);
        } catch (IOException e) {
            System.err.println("Error writing the binary inverted index file: " + e.getMessage());
        }
    }
}