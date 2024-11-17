package Benchmark;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


public class ExecuteStoringBenchmark {

    public static void main(String[] args) {
        runBinaryInvertedIndexTest();
        runTextInvertedIndexTest();
        runTextMetadataTest();
        runBinaryMetadataTest();
        runAllTests();
    }
    
    public static void runBinaryInvertedIndexTest() {
        try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName() + ".testBinaryII")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Binary-InvertedIndex_Store.json") 
                    .build();

            new Runner(options).run();
            System.out.println("Binary Inverted Index Benchmark completed. Results saved to Binary-InvertedIndex_Store.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runTextInvertedIndexTest() {
        try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName() + ".testFileII")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Text-InvertedIndex_Store.json") 
                    .build();

            new Runner(options).run();
            System.out.println("Text Inverted Index Benchmark completed. Results saved to Text-InvertedIndex_Store.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runTextMetadataTest() {
        try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName() + ".testFileMD")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Text-Metadata_Store.json") 
                    .build();

            new Runner(options).run();
            System.out.println("Text Metadata Benchmark completed. Results saved to Text-Metadata_Store.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runBinaryMetadataTest() {
        try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName() + ".testBinaryMD")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Binary-Metadata_Store.json") 
                    .build();

            new Runner(options).run();
            System.out.println("Binary Metadata Benchmark completed. Results saved to Binary-Metadata_Store.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runAllTests() {
        try {
            Options options = new OptionsBuilder()
                    .include(TestStoring.class.getSimpleName())
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("All-Tests_Store.json") 
                    .build();

            new Runner(options).run();
            System.out.println("All Benchmarks completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
