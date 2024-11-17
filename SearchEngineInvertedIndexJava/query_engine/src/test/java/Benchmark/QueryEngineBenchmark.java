package Benchmark;

import org.example.InvertedIndex.TXT.SingleWordSearchFile;
import org.example.InvertedIndex.MongoDB.SingleWordSearchMongoDB;
import org.example.InvertedIndex.Neo4J.SingleWordSearchNeo4j;
import org.example.InvertedIndex.Binary.SingleWordSearchBinary;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput) // Medir operaciones por segundo
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Mostrar resultados en milisegundos
@State(Scope.Thread) // Estado único para cada hilo
public class QueryEngineBenchmark {

    private Map<String, Map<String, Integer>> invertedIndex;
    private List<String> topWords;
    private List<String> bottomWords;
    private final String datalakePath = "datamart"; // Ruta al datalake

    // Ajustar según las rutas o configuraciones necesarias para MongoDB, Neo4j y Binary
    private final String mongoDBConnection = "mongodb://localhost:27017";
    private final String neo4jConnection = "bolt://localhost:7687";
    private final String binaryFilePath = datalakePath + "/inverted_index.dat";

    @Setup(Level.Iteration)
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

    // --------------------------- Benchmarks TXT -------------------------------------

    @Benchmark
    @OperationsPerInvocation(1)
    public void benchmarkTxtTopWords() {
        for (String word : topWords) {
            SingleWordSearchFile query = new SingleWordSearchFile(word, datalakePath + "/inverted_index.txt");
            query.execute();
        }
    }

    @Benchmark
    @OperationsPerInvocation(1)
    public void benchmarkTxtBottomWords() {
        for (String word : bottomWords) {
            SingleWordSearchFile query = new SingleWordSearchFile(word, datalakePath + "/inverted_index.txt");
            query.execute();
        }
    }

    // --------------------------- Benchmarks MongoDB ---------------------------------

    @Benchmark
    @OperationsPerInvocation(1)
    public void benchmarkMongoTopWords() {
        for (String word : topWords) {
            SingleWordSearchMongoDB query = new SingleWordSearchMongoDB(word);
            query.execute();
            try {
                Thread.sleep(100);  // 100ms delay between queries
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }

    @Benchmark
    @OperationsPerInvocation(1)
    public void benchmarkMongoBottomWords() {
        for (String word : bottomWords) {
            SingleWordSearchMongoDB query = new SingleWordSearchMongoDB(word);
            query.execute();
            try {
                Thread.sleep(100);  // 100ms delay between queries
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }

    // --------------------------- Benchmarks Neo4j -----------------------------------

    @Benchmark
    @OperationsPerInvocation(1)
    public void benchmarkNeo4jTopWords() {
        for (String word : topWords) {
            SingleWordSearchNeo4j query = new SingleWordSearchNeo4j(word);
            query.execute();
        }
    }

    @Benchmark
    @OperationsPerInvocation(1)
    public void benchmarkNeo4jBottomWords() {
        for (String word : bottomWords) {
            SingleWordSearchNeo4j query = new SingleWordSearchNeo4j(word);
            query.execute();
        }
    }

    // --------------------------- Benchmarks Binary ----------------------------------

    @Benchmark
    @OperationsPerInvocation(1)
    public void benchmarkBinaryTopWords() {
        for (String word : topWords) {
            SingleWordSearchBinary query = new SingleWordSearchBinary(word, binaryFilePath);
            query.execute();
        }
    }

    @Benchmark
    @OperationsPerInvocation(1)
    public void benchmarkBinaryBottomWords() {
        for (String word : bottomWords) {
            SingleWordSearchBinary query = new SingleWordSearchBinary(word, binaryFilePath);
            query.execute();
        }
    }

    // --------------------------- Métodos Auxiliares ---------------------------------

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