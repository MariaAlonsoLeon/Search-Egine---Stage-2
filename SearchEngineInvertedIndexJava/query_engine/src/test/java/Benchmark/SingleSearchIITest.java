package Benchmark;

import org.example.InvertedIndex.Binary.SingleWordSearchBinary;
import org.example.InvertedIndex.MongoDB.SingleWordSearchMongoDB;
import org.example.InvertedIndex.Neo4J.SingleWordSearchNeo4j;
import org.example.InvertedIndex.SearchCommand;
import org.example.InvertedIndex.TXT.SingleWordSearchFile;
import org.main.inverted_index.ProcessInvertedIndex;
import org.main.metadata.ProcessMetadata;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class SingleSearchIITest {
    private static final List<String> SEARCH_WORDS = Arrays.asList("freedom", "rights", "constitution", "democracy", "law");
    private Random random;

    private SearchCommand singleWordSearchFile;
    private SearchCommand singleWordSearchBinary;
    private SearchCommand singleWordSearchMongoDB;
    private SearchCommand singleWordSearchNeo4j;


    @Setup(Level.Trial)
    public void setup() {
        random = new Random();
        String random_word = randomSearchWord();
        singleWordSearchFile = new SingleWordSearchFile(random_word, "datamart/inverted_index.txt");
        singleWordSearchBinary = new SingleWordSearchBinary(random_word, "datamart/inverted_index.dat");
        singleWordSearchMongoDB = new SingleWordSearchMongoDB(random_word);
        singleWordSearchNeo4j = new SingleWordSearchNeo4j(random_word);
    }

    @Benchmark
    @OperationsPerInvocation(1)
    public void testQueryII() {
        //executeSearch(singleWordSearchFile);   // Test TXT
        waitBetweenExecutions();
        //executeSearch(singleWordSearchBinary); // Test Binary
        waitBetweenExecutions();
        //executeSearch(singleWordSearchMongoDB); // Test MongoDB
        waitBetweenExecutions();
        //executeSearch(singleWordSearchNeo4j);   // Test Neo4j
    }

    private void executeSearch(SearchCommand searchCommand) {
        //searchCommand.execute();
    }

    private void waitBetweenExecutions() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt status
            System.err.println("Execution interrupted: " + e.getMessage());
        }
    }




    /*@Benchmark
    public void testTXTQueryII() {
        singleWordSearchFile.execute();
    }

    @Benchmark
    public void testBinQueryII() {
        singleWordSearchBinary.execute();
    }

    @Benchmark
    public void testMongoQueryII() {
        singleWordSearchMongoDB.execute();
    }

    @Benchmark
    public void testNeoQueryII() {
        singleWordSearchNeo4j.execute();
    }*/

    private String randomSearchWord() {
        return SEARCH_WORDS.get(random.nextInt(SEARCH_WORDS.size()));
    }

}
