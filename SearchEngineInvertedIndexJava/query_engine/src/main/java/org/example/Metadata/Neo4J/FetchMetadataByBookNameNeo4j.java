package org.example.Metadata.Neo4J;

import org.example.Metadata.FetchMetadataCommand;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.HashMap;
import java.util.Map;

public class FetchMetadataByBookNameNeo4j implements FetchMetadataCommand {
    private static final String URI = "neo4j://localhost:7687";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "unodostres";
    private static final Driver driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));

    private final String bookName;

    public FetchMetadataByBookNameNeo4j(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public Map<String, String> execute() {
        Map<String, String> metadata = new HashMap<>();

        try (Session session = driver.session(SessionConfig.forDatabase("metadata"))) {
            String query = """
                    MATCH (f:File {name: $bookName})
                    RETURN f.author AS author, f.date AS date, f.language AS language
                    """;

            var result = session.run(query, Map.of("bookName", bookName));

            if (result.hasNext()) {
                Record record = result.next();

                String author = record.get("author").asString();
                String date = record.get("date").asString();
                String language = record.get("language").asString();

                metadata.put("author", author != null ? author : "Unknown");
                metadata.put("date", date != null ? date : "Unknown");
                metadata.put("language", language != null ? language : "Unknown");
            }
        } catch (Exception e) {
            System.err.println("Error fetching metadata for book: " + e.getMessage());
        }

        return metadata;
    }
}