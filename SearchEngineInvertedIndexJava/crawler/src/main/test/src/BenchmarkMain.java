package src;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkMain {

    public static void main(String[] args) throws RunnerException {
        runDownloaderBenchmarks();
        runDocumentStoreBenchmarks();
    }

    private static void runDownloaderBenchmarks() throws RunnerException {
        System.out.println("Running benchmarks for downloader...");

        /*Options downloaderOptions = new OptionsBuilder()
                .include(DownloaderBenchmark.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(5)
                .forks(1)
                .result("downloader_benchmark_results.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(downloaderOptions).run();*/
    }

    private static void runDocumentStoreBenchmarks() throws RunnerException {
        System.out.println("Running benchmarks for document storage...");

        Options documentStoreOptions = new OptionsBuilder()
                .include(DocumentStoreBenchmark.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(5)
                .forks(1)
                .result("document_store_benchmark_results.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(documentStoreOptions).run();
    }
}