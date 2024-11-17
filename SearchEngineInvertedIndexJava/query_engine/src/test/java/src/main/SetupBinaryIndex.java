package src.main;

import org.main.inverted_index.StoreBinary_II;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupBinaryIndex {

    private static final String DIRECTORY = "tests/test_repository";
    private static final String BINARY_INDEX_PATH = "datamart/inverted_index.dat";

    // Genera archivos de texto de muestra y almacena el índice invertido en un archivo binario
    public static void setupBooks(int count) {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (int i = 1; i <= count; i++) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIRECTORY + "/book_" + i + ".txt"))) {
                writer.write("Este es el contenido del libro " + i + ". Contiene texto sobre la libertad y los derechos. democracy\n");
            } catch (IOException e) {
                System.err.println("Error creating book file " + i + ": " + e.getMessage());
            }
        }

        // Crear índice invertido en binario (por ejemplo, usando StoreBinary_II)
        StoreBinary_II binaryStore = new StoreBinary_II();
        Map<String, Map<String, List<Integer>>> invertedIndex = createInvertedIndex(DIRECTORY);
        binaryStore.storeInvertedIndex(invertedIndex);
    }

    private static Map<String, Map<String, List<Integer>>> createInvertedIndex(String directoryPath) {
        // Implementar el procesamiento y construcción del índice invertido
        return new HashMap<>();
    }
}