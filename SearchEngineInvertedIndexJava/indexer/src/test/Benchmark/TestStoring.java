package Benchmark;

import org.main.inverted_index.ProcessInvertedIndex;
import org.main.inverted_index.StoreBinary_II;
import org.main.inverted_index.StoreText_II;
import org.main.metadata.ProcessMetadata;
import org.main.metadata.StoreBinary_MD;
import org.main.metadata.StoreText_MD;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class TestStoring {
    private Map<String, Map<String, List<Integer>>> INVERTEDINDEX;
    private Map<String, Map<String, String>> METADATA;
    private StoreBinary_II storeBinary_ii = new StoreBinary_II();
    private StoreText_II storeText_ii = new StoreText_II();
    private StoreBinary_MD storeBinary_md = new StoreBinary_MD();
    private StoreText_MD storeText_md = new StoreText_MD();


    @Param({"1", "2", "4", "6", "8", "10"}) 
    private int N;


    @Setup(Level.Trial)
    public void setup() {
        Map<String, String> book = BookReader.read_books(N);
        INVERTEDINDEX = ProcessInvertedIndex.createInvertedIndex(book);
        METADATA = ProcessMetadata.createMetadata(book);
    }

    @Benchmark
    public void testBinaryII() {
        storeBinary_ii.storeInvertedIndex(INVERTEDINDEX);
    }

    @Benchmark
    public void testFileII() {
        storeText_ii.storeInvertedIndex(INVERTEDINDEX);
    }

    @Benchmark
    public void testBinaryMD() {
        storeBinary_md.storeMetadata(METADATA);
    }

    @Benchmark
    public void testFileMD() {
        storeText_md.storeMetadata(METADATA);
    }
}
