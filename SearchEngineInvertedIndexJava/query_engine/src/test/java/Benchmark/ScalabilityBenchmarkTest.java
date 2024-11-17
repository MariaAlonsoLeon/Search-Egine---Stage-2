package Benchmark;


import org.example.InvertedIndex.TXT.SingleWordSearchFile;
import org.example.InvertedIndex.MongoDB.SingleWordSearchMongoDB;
import org.example.InvertedIndex.Neo4J.SingleWordSearchNeo4j;
import org.example.InvertedIndex.Binary.SingleWordSearchBinary;
import org.main.inverted_index.*;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class ScalabilityBenchmarkTest {

    private SingleWordSearchMongoDB mongo = null;
    private SingleWordSearchBinary bin = null;
    private SingleWordSearchFile file = null;
    private SingleWordSearchNeo4j neo = null;
    private ProcessInvertedIndex processInvertedIndex = new ProcessInvertedIndex();
    private StoreBinary_II storeBinary_ii = new StoreBinary_II();
    private StoreText_II storeText_ii = new StoreText_II();
    private StoreMongoDB_II storeMongoDB_ii = new StoreMongoDB_II();
    private StoreNeo4j_II storeNeo4j_ii = new StoreNeo4j_II();
    private final String datalakePath = "datamart"; // Ruta base del datalake
    private final String mongoDBConnection = "mongodb://localhost"; // Conexión a MongoDB
    private final String neo4jConnection = "bolt://localhost:7687"; // Conexión a Neo4j
    private final List<String> searchWords = List.of("freedom", "rights", "constitution", "democracy", "law");

    @Param({"10"})
    private int bookCount;

    private String randomWord;

    @Setup(Level.Trial)
    public void setup() {
        randomWord = getRandomWord();


        for (int i = 1; i <= bookCount; i++) {
            String bookContent = generateSampleBookContent(i);
            Map<String, String> books = new HashMap<>();
            books.put("book_" + i, bookContent);
            Map<String, Map<String, List<Integer>>> invertedIndex = processInvertedIndex.createInvertedIndex(books);
            storeText_ii.storeInvertedIndex(invertedIndex);
            storeBinary_ii.storeInvertedIndex(invertedIndex);
            storeMongoDB_ii.storeInvertedIndex(invertedIndex);
            storeNeo4j_ii.storeInvertedIndex(invertedIndex);
        }




        mongo = new SingleWordSearchMongoDB(randomWord);
        file = new SingleWordSearchFile(randomWord, datalakePath + "/inverted_index.txt");
        neo = new SingleWordSearchNeo4j(randomWord);
        bin = new SingleWordSearchBinary(randomWord, datalakePath + "/inverted_index.dat");

    }

    @Benchmark
    public void testFileSystemSearch() {
        //Map<String, List<String>> result = file.execute();
        //assert result != null;
    }

    @Benchmark
    public void testBinaryFileSearch() {
        //Map<String, List<String>> result = bin.execute();
        //assert result != null;
    }

    @Benchmark
    public void testMongoDBSearch() {
        //Map<String, List<String>> result = mongo.execute();
        //assert result != null;
    }

    @Benchmark
    public void testNeo4jSearch() {
        //Map<String, List<String>> result = neo.execute();
        //assert result != null;
    }

    private String getRandomWord() {
        Random random = new Random();
        return searchWords.get(random.nextInt(searchWords.size()));
    }

    private static String generateSampleBookContent(int bookId) {
        StringBuilder content = new StringBuilder();
        content.append("Book ID: ").append(bookId).append("\n");
        for (int i = 0; i < 100; i++) {
            content.append("This is a sample line about freedom, rights, and democracy. ");
            content.append("The constitution and law govern the principles of equality.\n");
        }
        return content.toString();
    }
}

