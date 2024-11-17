package Benchmark;

import org.example.InvertedIndex.TXT.SingleWordSearchFile;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) {
        /*try {
            Options opt = new OptionsBuilder()
                    .include(SingleSearchIITest.class.getSimpleName() + ".testTXTQueryII")
                    .forks(1)
                    .warmupIterations(2)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Text-InvertedIndex-SingleQuery.json")
                    .build();

            new Runner(opt).run();
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Options opt = new OptionsBuilder()
                    .include(SingleSearchIITest.class.getSimpleName() + ".testBinQueryII")
                    .forks(1)
                    .warmupIterations(2)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Text-Binary-SingleQuery.json")
                    .build();

            new Runner(opt).run();
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Options opt = new OptionsBuilder()
                    .include(SingleSearchIITest.class.getSimpleName() + ".testMongoQueryII")
                    .forks(1)
                    .warmupIterations(2)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Text-Mongo-SingleQuery.json")
                    .build();

            new Runner(opt).run();
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Options opt = new OptionsBuilder()
                    .include(SingleSearchIITest.class.getSimpleName() + ".testQueryII")
                    .forks(1)
                    .warmupIterations(2)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Test-SingleQuery.json")
                    .build();

            new Runner(opt).run();
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Options opt = new OptionsBuilder()
                    .include(BenchmarkCombined.class.getSimpleName() + ".benchmarkCreateMetadata")
                    .forks(1)
                    .warmupIterations(2)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Test-BenchmarkCombined-Metadata.json")
                    .build();

            new Runner(opt).run();
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            Options opt = new OptionsBuilder()
                    .include(ScalabilityBenchmarkTest.class.getSimpleName() + ".testBinaryFileSearch")
                    .forks(1)
                    .warmupIterations(2)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("test.json")
                    .build();

            new Runner(opt).run();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
