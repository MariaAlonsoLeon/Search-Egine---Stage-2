package src.main;

import org.example.InvertedIndex.MongoDB.SingleWordSearchMongoDB;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class QueryBenchmark {
    private final SingleWordSearchMongoDB singleMongo = new SingleWordSearchMongoDB("prepare");


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSingleWordNeo4J() {

    }
}
