package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileIndexer {
    // Indexer starts working at the same moment as the whole application so at the beginning there may be no books to index
    // That's why every time it works, it indexes only existing books, and before indexing each of them it checks if it's not already indexed
    // If yes, just skip to not do the same job twice
    // TIME_BREAK_SECONDS is a time between each iteration of indexing

    private static final String DOCUMENT_REPOSITORY = "datalake_document_repository/books";
    private static final String INVERTED_INDEX_REPOSITORY = "datalake_inverted_index";
    private static final Integer TIME_BREAK_SECONDS = 10;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // for now hardcoded, can be changed to some library (e.g.Lucene)
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he",
            "in", "is", "it", "its", "of", "on", "that", "the", "to", "was", "were", "will", "with"
    ));

    public static void main(String[] args) {
        FileIndexer indexer = new FileIndexer();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(indexer::indexDocuments, 0, TIME_BREAK_SECONDS, TimeUnit.SECONDS);
    }

    private void indexDocuments() {
        File documentRepository = new File(DOCUMENT_REPOSITORY);

        if (!documentRepository.exists() || !documentRepository.isDirectory()) {
            System.out.println("Document repository does not exist or is not a directory.");
            return;
        }

        for (File dateFolder : Objects.requireNonNull(documentRepository.listFiles(File::isDirectory))) {
            File invertedIndexDateFolder = new File(INVERTED_INDEX_REPOSITORY, dateFolder.getName());
            invertedIndexDateFolder.mkdirs();

            for (File txtFile : Objects.requireNonNull(dateFolder.listFiles((dir, name) -> name.endsWith(".txt")))) {
                String jsonFileName = txtFile.getName().replace(".txt", ".json");
                File jsonFile = new File(invertedIndexDateFolder, jsonFileName);

                if (jsonFile.exists()) {
                    System.out.println("Index for file already exists, skipping: " + jsonFileName);
                    continue;
                }

                try {
                    Map<String, WordData> invertedIndex = createInvertedIndex(txtFile);
                    objectMapper.writeValue(jsonFile, invertedIndex);
                    System.out.println("Inverted index created for file: " + txtFile.getName());
                } catch (IOException e) {
                    System.out.println("Error processing file: " + txtFile.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private Map<String, WordData> createInvertedIndex(File txtFile) throws IOException {
        Map<String, WordData> invertedIndex = new HashMap<>();
        try (FileReader fileReader = new FileReader(txtFile);
             Scanner scanner = new Scanner(fileReader)) {
            int position = 0;

            while (scanner.hasNext()) {
                String word = scanner.next().replaceAll("\\W", "").toLowerCase();
                position++;

                if (!word.isEmpty() && !STOP_WORDS.contains(word)) {
                    WordData wordData = invertedIndex.computeIfAbsent(word, k -> new WordData());
                    wordData.addPosition(position);
                }
            }
        }
        return invertedIndex;
    }

    static class WordData {
        private int frequency;
        private List<Integer> positions;

        public WordData() {
            this.frequency = 0;
            this.positions = new ArrayList<>();
        }

        public void addPosition(int position) {
            frequency++;
            positions.add(position);
        }

        public int getFrequency() {
            return frequency;
        }

        public List<Integer> getPositions() {
            return positions;
        }
    }
}
