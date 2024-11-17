package src;

import org.example.crawler.GutenbergDocumentDownloader;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class DownloaderBenchmark {
    @Param({"1234", "5678"})
    int id;

    private GutenbergDocumentDownloader downloader;

    @Setup(Level.Trial)
    public void setup() {
        downloader = new GutenbergDocumentDownloader();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public String testDownloadBook() throws IOException {
        int bookId = id;
        return downloader.downloadDocument(bookId).text();
    }
}