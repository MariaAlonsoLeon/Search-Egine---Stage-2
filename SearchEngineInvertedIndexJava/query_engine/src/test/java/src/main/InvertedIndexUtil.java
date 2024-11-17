package src.main;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class InvertedIndexUtil {

    // Lee un archivo y retorna un mapa con el nombre del archivo como clave y el contenido como valor
    public static Map<String, String> readTxtAsMap(String filePath) {
        Map<String, String> fileContentMap = new HashMap<>();
        String filename = Paths.get(filePath).getFileName().toString().split("\\.")[0];

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            fileContentMap.put(filename, content);
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            fileContentMap.put(filename, "ERROR_READING_FILE");
        }

        return fileContentMap;
    }
}