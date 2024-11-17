package TimeModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.InvertedIndex.TXT.SingleWordSearchFile;
import org.example.InvertedIndex.MongoDB.SingleWordSearchMongoDB;
import org.example.InvertedIndex.Neo4J.SingleWordSearchNeo4j;
import org.example.InvertedIndex.Binary.SingleWordSearchBinary;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FrequencyTest {

    private Map<String, Map<String, Integer>> invertedIndex;
    private List<String> topWords;
    private List<String> bottomWords;
    private final String datalakePath = "datamart"; // Ruta al datalake
    private final String outputFile = "benchmark_results.json"; // JSON output file path

    private final String mongoDBConnection = "mongodb://localhost:27017";
    private final String neo4jConnection = "bolt://localhost:7687";
    private final String binaryFilePath = datalakePath + "/inverted_index.dat";

    private final Map<String, Double> benchmarkResults = new LinkedHashMap<>();

    public static void main(String[] args) {
        FrequencyTest benchmark = new FrequencyTest();
        benchmark.setup();

        System.out.println("=== TXT Benchmark (Top Words) ===");
        benchmark.runAndMeasure("TXT_TopWords", benchmark::benchmarkTxtTopWords);

        System.out.println("\n=== TXT Benchmark (Bottom Words) ===");
        benchmark.runAndMeasure("TXT_BottomWords", benchmark::benchmarkTxtBottomWords);

        System.out.println("\n=== MongoDB Benchmark (Top Words) ===");
        benchmark.runAndMeasure("MongoDB_TopWords", benchmark::benchmarkMongoTopWords);

        System.out.println("\n=== MongoDB Benchmark (Bottom Words) ===");
        benchmark.runAndMeasure("MongoDB_BottomWords", benchmark::benchmarkMongoBottomWords);

        System.out.println("\n=== Neo4j Benchmark (Top Words) ===");
        benchmark.runAndMeasure("Neo4j_TopWords", benchmark::benchmarkNeo4jTopWords);

        System.out.println("\n=== Neo4j Benchmark (Bottom Words) ===");
        benchmark.runAndMeasure("Neo4j_BottomWords", benchmark::benchmarkNeo4jBottomWords);

        System.out.println("\n=== Binary Benchmark (Top Words) ===");
        benchmark.runAndMeasure("Binary_TopWords", benchmark::benchmarkBinaryTopWords);

        System.out.println("\n=== Binary Benchmark (Bottom Words) ===");
        benchmark.runAndMeasure("Binary_BottomWords", benchmark::benchmarkBinaryBottomWords);

        benchmark.saveResultsToJson();
    }

    public void setup() {
        // Procesar documentos y generar el índice invertido
        Map<String, String> books = loadBooksFromFolder(datalakePath);
        invertedIndex = createInvertedIndex(books);

        // Calcular las palabras más frecuentes y menos frecuentes
        Map<String, Integer> wordFrequencies = calculateWordFrequencies(invertedIndex);
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordFrequencies.entrySet());
        sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        // Obtener las 5 más frecuentes y 5 menos frecuentes
        topWords = sortedEntries.subList(0, Math.min(5, sortedEntries.size()))
                .stream().map(Map.Entry::getKey).toList();
        bottomWords = sortedEntries.subList(Math.max(0, sortedEntries.size() - 5), sortedEntries.size())
                .stream().map(Map.Entry::getKey).toList();
    }

    // --------------------------- Timing Utility ---------------------------------
    private void runAndMeasure(String benchmarkName, Runnable benchmark) {
        long startTime = System.nanoTime();
        benchmark.run();
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        benchmarkResults.put(benchmarkName, durationMs);
        System.out.printf("Execution time for %s: %.3f ms%n", benchmarkName, durationMs);
    }

    private void saveResultsToJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(Paths.get(outputFile).toFile(), benchmarkResults);
            System.out.println("\nBenchmark results saved to " + outputFile);
        } catch (IOException e) {
            System.err.println("Error saving benchmark results to JSON: " + e.getMessage());
        }
    }

    // --------------------------- Benchmarks TXT ---------------------------------

    public void benchmarkTxtTopWords() {
        for (String word : topWords) {
            SingleWordSearchFile query = new SingleWordSearchFile(word, datalakePath + "/inverted_index.txt");
            query.execute();
        }
    }

    public void benchmarkTxtBottomWords() {
        for (String word : bottomWords) {
            SingleWordSearchFile query = new SingleWordSearchFile(word, datalakePath + "/inverted_index.txt");
            query.execute();
        }
    }

    // --------------------------- Benchmarks MongoDB -----------------------------

    public void benchmarkMongoTopWords() {
        for (String word : topWords) {
            SingleWordSearchMongoDB query = new SingleWordSearchMongoDB(word);
            query.execute();
        }
    }

    public void benchmarkMongoBottomWords() {
        for (String word : bottomWords) {
            SingleWordSearchMongoDB query = new SingleWordSearchMongoDB(word);
            query.execute();
        }
    }

    // --------------------------- Benchmarks Neo4j -------------------------------

    public void benchmarkNeo4jTopWords() {
        for (String word : topWords) {
            SingleWordSearchNeo4j query = new SingleWordSearchNeo4j(word);
            query.execute();
        }
    }

    public void benchmarkNeo4jBottomWords() {
        for (String word : bottomWords) {
            SingleWordSearchNeo4j query = new SingleWordSearchNeo4j(word);
            query.execute();
        }
    }

    // --------------------------- Benchmarks Binary ------------------------------

    public void benchmarkBinaryTopWords() {
        for (String word : topWords) {
            SingleWordSearchBinary query = new SingleWordSearchBinary(word, binaryFilePath);
            query.execute();
        }
    }

    public void benchmarkBinaryBottomWords() {
        for (String word : bottomWords) {
            SingleWordSearchBinary query = new SingleWordSearchBinary(word, binaryFilePath);
            query.execute();
        }
    }

    // --------------------------- Métodos Auxiliares -----------------------------

    private Map<String, String> loadBooksFromFolder(String folderPath) {
        Map<String, String> documents = new HashMap<>();
        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(filePath -> {
                        try {
                            String content = Files.readString(filePath);
                            documents.put(filePath.getFileName().toString(), content);
                        } catch (IOException e) {
                            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error accessing folder: " + folderPath + " - " + e.getMessage());
        }
        return documents;
    }

    private Map<String, Map<String, Integer>> createInvertedIndex(Map<String, String> books) {
        Map<String, Map<String, Integer>> index = new HashMap<>();

        for (Map.Entry<String, String> book : books.entrySet()) {
            String bookName = book.getKey();
            String content = book.getValue().toLowerCase();
            String[] words = content.split("\\W+");

            for (String word : words) {
                if (!word.isEmpty()) {
                    index.computeIfAbsent(word, k -> new HashMap<>())
                            .merge(bookName, 1, Integer::sum);
                }
            }
        }
        return index;
    }

    private Map<String, Integer> calculateWordFrequencies(Map<String, Map<String, Integer>> index) {
        Map<String, Integer> frequencies = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer>> entry : index.entrySet()) {
            String word = entry.getKey();
            int totalFrequency = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            frequencies.put(word, totalFrequency);
        }

        return frequencies;
    }
}
