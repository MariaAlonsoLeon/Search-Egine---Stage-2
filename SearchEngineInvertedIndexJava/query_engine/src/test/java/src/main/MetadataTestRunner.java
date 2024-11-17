package src.main;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class MetadataTestRunner {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(QueryMetadataBenchmark.class.getSimpleName() + ".testSearchWordMongoDB")
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(10)
                .resultFormat(ResultFormatType.JSON)
                .result("MongoDB-Metadata-SearchWord.json")
                .build();

        new Runner(options).run();
    }
}
