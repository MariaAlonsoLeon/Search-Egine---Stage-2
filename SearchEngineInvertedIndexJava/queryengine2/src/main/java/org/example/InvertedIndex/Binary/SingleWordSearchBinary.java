package org.example.InvertedIndex.Binary;

import org.example.InvertedIndex.SearchCommand;

import java.io.*;
import java.util.*;

public class SingleWordSearchBinary implements SearchCommand {
    private final String word;
    private final String filePath;

    public SingleWordSearchBinary(String word, String filePath) {
        this.word = word.toLowerCase();
        this.filePath = filePath;
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> results = new HashMap<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            while (dis.available() > 0) {
                // Leer el byte que indica el campo (word)
                byte fieldCode = dis.readByte();
                if (fieldCode != 1) { // Si no es el campo "word", saltamos
                    dis.readUTF(); // Leer y descartar los datos
                    continue;
                }
                String currentWord = dis.readUTF();

                // Si la palabra no coincide con la que buscamos, saltamos al siguiente registro
                if (!currentWord.equals(word)) {
                    skipDocumentEntries(dis); // Saltamos los documentos de esta palabra
                    continue;
                }

                // Si encontramos la palabra, leemos los documentos asociados
                while (dis.available() > 0) {
                    fieldCode = dis.readByte();
                    if (fieldCode == 0) {
                        break; // Fin del registro de la palabra
                    }

                    // Leer los documentos y las posiciones
                    if (fieldCode == 2) { // Campo "document"
                        String documentName = dis.readUTF();
                        fieldCode = dis.readByte(); // Leer siguiente campo ("positions")

                        if (fieldCode == 3) { // Campo "positions"
                            int positionsSize = dis.readInt();
                            List<String> positionList = new ArrayList<>();

                            for (int i = 0; i < positionsSize; i++) {
                                int position = dis.readInt();
                                positionList.add(String.valueOf(position));
                            }

                            // Añadir el resultado al mapa
                            results.putIfAbsent(documentName, new ArrayList<>());
                            results.get(documentName).add(String.join(", ", positionList));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the binary inverted index file: " + e.getMessage());
        }

        return results;
    }

    // Método para saltar la lectura de los documentos de una palabra
    private void skipDocumentEntries(DataInputStream dis) throws IOException {
        byte fieldCode;
        while (dis.available() > 0) {
            fieldCode = dis.readByte();
            if (fieldCode == 0) { // Fin del registro de palabra
                break;
            }
            if (fieldCode == 2) { // Campo "document"
                dis.readUTF(); // Leer y descartar el nombre del documento
                fieldCode = dis.readByte();
                if (fieldCode == 3) { // Campo "positions"
                    int positionsSize = dis.readInt();
                    for (int i = 0; i < positionsSize; i++) {
                        dis.readInt(); // Leer y descartar las posiciones
                    }
                }
            }
        }
    }
}