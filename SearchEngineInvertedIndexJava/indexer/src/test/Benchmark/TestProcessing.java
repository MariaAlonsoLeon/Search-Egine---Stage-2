package Benchmark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.main.inverted_index.ProcessInvertedIndex;
import org.main.metadata.ProcessMetadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class TestProcessing {

    private Map<String, String> books = new HashMap<>();
    private ProcessInvertedIndex processInvertedIndex = new ProcessInvertedIndex();
    private ProcessMetadata processMetadata = new ProcessMetadata();

    public Map<String, String> loadBooksFromFolder(String folderPath) {
        Map<String, String> documents = new HashMap<>();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + folderPath);
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                try {
                    String content = Files.readString(file.toPath());
                    documents.put(file.getName(), content);
                } catch (IOException e) {
                    System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        return documents;
    }

    public double measureInvertedIndexProcessing(String folderPath, int repetitions) {
        books = loadBooksFromFolder(folderPath);

        long totalTimeInNano = 0;

        for (int i = 0; i < repetitions; i++) {
            long startTime = System.nanoTime();
            processInvertedIndex.createInvertedIndex(books, "indexer\\src\\main\\resources\\stopwords.txt");
            long endTime = System.nanoTime();
            totalTimeInNano += (endTime - startTime);
        }

        return (double) totalTimeInNano / repetitions / 1_000_000; 
    }

    public double measureMetadataProcessing(String folderPath, int repetitions) {
        books = loadBooksFromFolder(folderPath);

        long totalTimeInNano = 0;

        for (int i = 0; i < repetitions; i++) {
            long startTime = System.nanoTime();
            processMetadata.createMetadata(books);
            long endTime = System.nanoTime();
            totalTimeInNano += (endTime - startTime);
        }

        return (double) totalTimeInNano / repetitions / 1_000_000; 
    }

    public void saveResultsToJson(Map<String, Double> results, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(results, writer);
            System.out.println("Results saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving results to JSON: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TestProcessing test = new TestProcessing();

        String folderPath = "datalake/20241116";
        String outputPath = "processing_results.json";
        int repetitions = 5;

        System.out.println("Measuring processing times from datalake...");

        double invertedIndexTime = test.measureInvertedIndexProcessing(folderPath, repetitions);
        double metadataTime = test.measureMetadataProcessing(folderPath, repetitions);

        Map<String, Double> results = new HashMap<>();
        results.put("Average Inverted Index Processing Time (ms)", invertedIndexTime);
        results.put("Average Metadata Processing Time (ms)", metadataTime);

        test.saveResultsToJson(results, outputPath);
    }
}
