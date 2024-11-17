package src;

import org.example.crawler.FileDocumentStore;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class DocumentStoreBenchmark {
    @Param({"1", "10", "100"})
    int number;

    private FileDocumentStore documentStore;

    @Setup(Level.Trial)
    public void setup() {
        this.documentStore = new FileDocumentStore();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSaveDocument() throws IOException {
        for (int i = 0; i < number; i++){
            String document = "Some content to simulate the document";
            String composedId = "2024-11-10 10-00-00/1";
            documentStore.saveDocument(document, composedId);
        }
    }
}
