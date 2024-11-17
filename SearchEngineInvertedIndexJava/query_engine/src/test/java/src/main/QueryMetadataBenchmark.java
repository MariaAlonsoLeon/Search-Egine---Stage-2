package src.main;

import org.example.InvertedIndex.MongoDB.SingleWordSearchMongoDB;
//import org.example.all.SingleWordSearchNeo4J;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class QueryMetadataBenchmark {

    private static final String URI = "neo4j://localhost:7687";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "unodostres";
    //private static final Driver neo4jDriver = GraphDatabase.driver(URI, org.neo4j.driver.AuthTokens.basic(USER, PASSWORD));
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    public void testSearchWordMongoDB() {
        SingleWordSearchMongoDB searchMongoDB = new SingleWordSearchMongoDB("prepare");
        searchMongoDB.execute();
    }

    /*@Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    @Timeout(time = 10, timeUnit = TimeUnit.SECONDS)
    public void testSearchWordNeo4j(BenchmarkState state) {
        // Inicializar con una palabra aleatoria antes de cada ejecución
        state.initializeSearch();
        // Benchmark para la búsqueda de palabras en Neo4j
        state.searchNeo4j.execute();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    @Timeout(time = 10, timeUnit = TimeUnit.SECONDS)
    public void testFetchMetadataMongoDB(BenchmarkState state) {
        // Benchmark para obtener metadatos de MongoDB
        state.fetchMetadataNeo4j.execute();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    @Timeout(time = 10, timeUnit = TimeUnit.SECONDS)
    public void testFetchMetadataNeo4j(BenchmarkState state) {
        // Benchmark para obtener metadatos de Neo4j
        state.fetchMetadataNeo4j.execute();
    }*/

}
