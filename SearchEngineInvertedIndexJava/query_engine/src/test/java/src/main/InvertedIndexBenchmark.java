package src.main;

import org.example.InvertedIndex.SearchCommand;
import org.openjdk.jmh.annotations.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@State(Scope.Benchmark)
public class InvertedIndexBenchmark {

    /*private static final List<String> SEARCH_WORDS = Arrays.asList("freedom", "rights", "constitution", "democracy", "law");
    private static final int NUM_BOOKS = 100;

    @Param({"filesystem", "mongodb", "neo4j"})
    private String storageType;

    private Random random;
    private SearchCommand searchCommand;

    @Setup(Level.Trial)
    public void setup() {
        random = new Random();

        switch (storageType) {
            case "filesystem":
                SetupBinaryIndex.setupBooks(NUM_BOOKS);  // Configurar libros para el sistema de archivos
                searchCommand = new SingleWordSearchFile(randomSearchWord(), "datamart/inverted_index.dat");
                break;

            case "mongodb":
                SetupMongoDBIndex.setupBooks(NUM_BOOKS);  // Configurar libros para MongoDB
                searchCommand = new SingleWordSearchMongoDB(randomSearchWord()); // Implementar en MongoDB
                break;

            case "neo4j":
                SetupNeo4jIndex.setupBooks(NUM_BOOKS);  // Configurar libros para Neo4j
                searchCommand = new SingleWordSearchNeo4j(randomSearchWord()); // Implementar en Neo4j
                break;
        }
    }

    // Método para ejecutar las pruebas de rendimiento de búsqueda
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Map<String, List<String>> searchWordBenchmark() {
        return searchCommand.execute();  // Ejecuta la búsqueda en la estructura seleccionada
    }

    // Generar una palabra aleatoria para búsqueda
    private String randomSearchWord() {
        return SEARCH_WORDS.get(random.nextInt(SEARCH_WORDS.size()));
    }*/
}