package Benchmark;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class ExecuteStoringBenchmark {

    public static void main(String[] args) {

        /*try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName() + ".testBinaryII")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Binary-InvertedIndex_Store.json") // Guarda los resultados en un archivo JSON
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to Benchmark-Metadata-Processing.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName() + ".testFileII")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Text-InvertedIndex_Store.json") // Guarda los resultados en un archivo JSON
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to Benchmark-Metadata-Processing.json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName() + ".testFileMD")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Text-Metadata_Store.json") // Guarda los resultados en un archivo JSON
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to Benchmark-Metadata-Processing.json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName() + ".testBinaryMD")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    //.result("Binary-Metadata_Store.json") // Guarda los resultados en un archivo JSON
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to Benchmark-Metadata-Processing.json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName())
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    //.result("Binary-Metadata_Store.json") // Guarda los resultados en un archivo JSON
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to Benchmark-Metadata-Processing.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
