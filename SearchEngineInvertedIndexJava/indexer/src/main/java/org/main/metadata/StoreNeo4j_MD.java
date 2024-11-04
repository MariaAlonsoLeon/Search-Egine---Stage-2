package org.main.metadata;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.util.Map;

public class StoreNeo4j_MD implements StoreInterface_MD {
    private static final String URI = "neo4j://localhost:7687";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "unodostres";
    private static final Driver driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));

    @Override
    public void storeMetadata(Map<String, Map<String, String>> metadata) {
        try (Session session = driver.session()) {
            for (Map.Entry<String, Map<String, String>> entry : metadata.entrySet()) {
                String fileName = entry.getKey();
                Map<String, String> details = entry.getValue();
                String author = details.getOrDefault("author", "Unknown");
                String date = details.getOrDefault("date", "Unknown");
                String language = details.getOrDefault("language", "Unknown");

                session.writeTransaction(tx -> {
                    tx.run(
                            "MERGE (f:File {name: $fileName}) " +
                                    "ON CREATE SET f.author = $author, f.date = $date, f.language = $language " +
                                    "RETURN f",
                            Map.of(
                                    "fileName", fileName,
                                    "author", author,
                                    "date", date,
                                    "language", language
                            )
                    );
                    return null;
                });
            }
        } catch (Exception e) {
            System.err.println("Error storing metadata in Neo4j: " + e.getMessage());
        }
    }
}
