package Benchmark;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class ExecuteProcessingBenchmark {

    public static void main(String[] args) {
        try {
            Options options = new OptionsBuilder()
                    .include(TestProcessing.class.getSimpleName() + ".testMetadataProcessing")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Benchmark-Metadata-Processing.json") // Guarda los resultados en un archivo JSON
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to Benchmark-Metadata-Processing.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Options options = new OptionsBuilder()
                    .include(TestProcessing.class.getSimpleName() + ".testInvertedIndexProcessing")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Benchmark-InvertedIndex-Processing.json") // Guarda los resultados en un archivo JSON
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to Benchmark-InvertedIndex-Processing.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
