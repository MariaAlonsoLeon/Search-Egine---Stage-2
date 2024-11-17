package TimeModule;

import org.example.InvertedIndex.TXT.SingleWordSearchFile;
import org.example.InvertedIndex.MongoDB.SingleWordSearchMongoDB;
import org.example.InvertedIndex.Neo4J.SingleWordSearchNeo4j;
import org.example.InvertedIndex.Binary.SingleWordSearchBinary;
import org.main.inverted_index.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ScalabilityTest {

    private SingleWordSearchMongoDB mongo = null;
    private SingleWordSearchBinary bin = null;
    private SingleWordSearchFile file = null;
    private SingleWordSearchNeo4j neo = null;
    private ProcessInvertedIndex processInvertedIndex = new ProcessInvertedIndex();
    private StoreBinary_II storeBinary_ii = new StoreBinary_II();
    private StoreText_II storeText_ii = new StoreText_II();
    private StoreMongoDB_II storeMongoDB_ii = new StoreMongoDB_II();
    private StoreNeo4j_II storeNeo4j_ii = new StoreNeo4j_II();
    private final String datalakePath = "datamart";
    private final String mongoDBConnection = "mongodb://localhost";
    private final String neo4jConnection = "bolt://localhost:7687";
    private final List<String> searchWords = List.of("freedom", "rights", "constitution", "democracy", "law");

    private int bookCount;
    private String randomWord;

    public void setup(int bookCount) {
        this.bookCount = bookCount;
        randomWord = getRandomWord();

        try {
            Path targetFolder = Paths.get("TestRepository");
            Files.createDirectories(targetFolder);

            for (int i = 1; i <= bookCount; i++) {
                String bookContent = generateSampleBookContent(i);
                Map<String, String> book = new HashMap<>();
                book.put("book_" + i, bookContent);
                Map<String, Map<String, List<Integer>>> invertedIndex = processInvertedIndex.createInvertedIndex(book, "indexer/src/main/resources/stopwords.txt");
                storeText_ii.storeInvertedIndex(invertedIndex);
                storeBinary_ii.storeInvertedIndex(invertedIndex);
                storeMongoDB_ii.storeInvertedIndex(invertedIndex);
                storeNeo4j_ii.storeInvertedIndex(invertedIndex);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error setting up books in file system: " + e.getMessage(), e);
        }

        mongo = new SingleWordSearchMongoDB(randomWord);
        file = new SingleWordSearchFile(randomWord, datalakePath + "/inverted_index.txt");
        neo = new SingleWordSearchNeo4j(randomWord);
        bin = new SingleWordSearchBinary(randomWord, datalakePath + "/inverted_index.dat");
    }

    public long testFileSystemSearch() {
        long startTime = System.nanoTime();
        Map<String, List<String>> result = file.execute();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    public long testBinaryFileSearch() {
        long startTime = System.nanoTime();
        Map<String, List<String>> result = bin.execute();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    public long testMongoDBSearch() {
        long startTime = System.nanoTime();
        Map<String, List<String>> result = mongo.execute();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    public long testNeo4jSearch() {
        long startTime = System.nanoTime();
        Map<String, List<String>> result = neo.execute();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
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

    private void saveResultsToJson(List<JSONObject> results) {
        JSONObject output = new JSONObject();
        output.put("results", new JSONArray(results));

        try (FileWriter file = new FileWriter("benchmark_results.json")) {
            file.write(output.toString(4));
            System.out.println("Results saved to benchmark_results.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ScalabilityTest test = new ScalabilityTest();

        int[] testParams = {10, 50, 100, 500, 1000};

        List<JSONObject> results = new ArrayList<>();

        for (int books : testParams) {
            System.out.println("Testing with " + books + " books:");
            test.setup(books);

            JSONObject testResult = new JSONObject();
            testResult.put("bookCount", books);

            testResult.put("FileSystemSearch", test.testFileSystemSearch());
            testResult.put("BinaryFileSearch", test.testBinaryFileSearch());
            testResult.put("MongoDBSearch", test.testMongoDBSearch());
            testResult.put("Neo4jSearch", test.testNeo4jSearch());

            results.add(testResult);

            System.out.println();
        }

        test.saveResultsToJson(results);
    }
}
