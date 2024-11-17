package org.example.InvertedIndex.Neo4J;

import org.example.InvertedIndex.SearchCommand;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SearchWordWithContextNeo4J implements SearchCommand {
    private final String word;
    private final Driver driver;
    private final String documentFolder;
    private final int contextSize;

    public SearchWordWithContextNeo4J(String word, Driver driver, String documentFolder, int contextSize) {
        this.word = word.toLowerCase();
        this.driver = driver;
        this.documentFolder = documentFolder;
        this.contextSize = contextSize;
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> contexts = new HashMap<>();

        try (Session session = driver.session(SessionConfig.forDatabase("invertedindex"))) {
            String query = """
                    MATCH (w:Word {word: $word})-[r:APPEARS_IN]->(d:Document)
                    RETURN d.title AS document, r.positions AS positions""";
            var result = session.run(query, Map.of("word", word));

            Map<String, List<Integer>> invertedIndex = new HashMap<>();
            while (result.hasNext()) {
                Record record = result.next();
                String document = record.get("document").asString();
                List<Integer> positions = record.get("positions").asList(Value::asInt);

                invertedIndex.put(document, positions);
            }

            for (Map.Entry<String, List<Integer>> entry : invertedIndex.entrySet()) {
                String filename = entry.getKey();
                List<Integer> positions = entry.getValue();

                String filePath = Paths.get(documentFolder, filename + ".txt").toString();

                try {
                    String content = Files.readString(Paths.get(filePath)).toLowerCase();
                    String[] words = content.split("\\W+");
                    List<String> documentContexts = new ArrayList<>();

                    for (int pos : positions) {
                        int start = Math.max(0, pos - this.contextSize);
                        int end = Math.min(words.length, pos + this.contextSize);
                        String context = String.join(" ", Arrays.asList(words).subList(start, end));

                        documentContexts.add(context);
                    }

                    contexts.put(filename, documentContexts);
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo " + filePath + ": " + e.getMessage());
                }
            }
        }

        return contexts.isEmpty() ? Map.of() : contexts;
    }
}
