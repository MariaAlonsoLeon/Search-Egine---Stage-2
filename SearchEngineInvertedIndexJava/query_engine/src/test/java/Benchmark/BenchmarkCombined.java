package Benchmark;

import org.main.inverted_index.ProcessInvertedIndex;
import org.main.metadata.ProcessMetadata;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkCombined {

    private Map<String, String> sampleBooks;

    @Setup(Level.Iteration)
    public void setup() {
        String datalakePath = "datalake/20241111"; // Ruta de la carpeta
        sampleBooks = readDocumentsFromFolder(datalakePath);
    }

    /**
     * Lee todos los archivos .txt de una carpeta y los guarda en un mapa.
     *
     * @param folderPath Ruta de la carpeta donde están los archivos .txt.
     * @return Un mapa donde la clave es el nombre del archivo y el valor su contenido.
     */
    private Map<String, String> readDocumentsFromFolder(String folderPath) {
        Map<String, String> documents = new HashMap<>();
        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(filePath -> {
                        try {
                            String content = Files.readString(filePath);
                            documents.put(filePath.toString(), content);
                        } catch (IOException e) {
                            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error accessing folder: " + folderPath + " - " + e.getMessage());
        }
        return documents;
    }

    @Benchmark
    public void benchmarkCreateInvertedIndex() {
        ProcessInvertedIndex.createInvertedIndex(sampleBooks, "");
    }

    @Benchmark
    public void benchmarkCreateMetadata() {
        ProcessMetadata.createMetadata(sampleBooks);
    }
}