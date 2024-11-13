package org.example.all;

import org.example.InvertedIndex.SearchCommand;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleWordSearchBinary implements SearchCommand {
    private final String word;
    private static final String FILE_PATH = "inverted_index.bin"; // El archivo binario donde se almacena el índice

    public SingleWordSearchBinary(String word) {
        this.word = word;
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> results = new HashMap<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(FILE_PATH))) {

            // Verificación del archivo binario antes de leer
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                System.err.println("El archivo binario no existe: " + FILE_PATH);
                return results;
            }
            if (!file.canRead()) {
                System.err.println("No se puede leer el archivo binario: " + FILE_PATH);
                return results;
            }

            // Iterar por el archivo binario en busca de la palabra
            while (dis.available() > 0) {
                // Leer la longitud de la palabra
                int wordLength = dis.readInt();
                System.out.println("Longitud de la palabra: " + wordLength);  // Depuración

                // Leer los bytes de la palabra
                byte[] wordBytes = new byte[wordLength];
                dis.readFully(wordBytes);
                String currentWord = new String(wordBytes);
                System.out.println("Palabra leída: " + currentWord);  // Depuración

                // Si encontramos la palabra, proceder a leer los libros y las posiciones
                if (currentWord.equals(word)) {
                    // Leer el número de libros asociados a la palabra
                    int numBooks = dis.readInt();
                    System.out.println("Número de libros encontrados: " + numBooks);  // Depuración

                    for (int i = 0; i < numBooks; i++) {
                        // Leer el nombre del libro (longitud + caracteres)
                        int bookLength = dis.readInt();
                        byte[] bookBytes = new byte[bookLength];
                        dis.readFully(bookBytes);
                        String bookTitle = new String(bookBytes);

                        // Leer el número de posiciones en este libro
                        int numPositions = dis.readInt();
                        List<String> positionStrings = new ArrayList<>();

                        // Leer las posiciones usando la codificación variable
                        for (int j = 0; j < numPositions; j++) {
                            int position = readVarInt(dis);
                            positionStrings.add(Integer.toString(position));
                        }

                        // Almacenar los resultados (libro y posiciones)
                        results.put(bookTitle, positionStrings);
                    }

                    // Si encontramos la palabra, ya podemos salir del loop
                    break;
                } else {
                    // Si no encontramos la palabra, leer los datos del siguiente
                    // índice para avanzar en el archivo
                    int numBooks = dis.readInt();
                    for (int i = 0; i < numBooks; i++) {
                        // Leer el nombre del libro y las posiciones para saltarlas
                        int bookLength = dis.readInt();
                        dis.skipBytes(bookLength); // Saltamos el nombre del libro
                        int numPositions = dis.readInt();
                        dis.skipBytes(numPositions * 4); // Saltamos las posiciones (4 bytes por posición)
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Imprime la traza completa
            System.err.println("Error ejecutando la búsqueda en el archivo binario: " + e.getMessage());
        }

        return results;
    }

    // Función para leer enteros codificados con Variable Byte Encoding
    private int readVarInt(DataInputStream dis) throws IOException {
        int value = 0;
        int shift = 0;
        byte byteRead;
        do {
            byteRead = dis.readByte();
            value |= (byteRead & 0x7F) << shift;
            shift += 7;
        } while ((byteRead & 0x80) != 0);
        return value;
    }
}
