package org.main.inverted_index;

import org.neo4j.driver.*;

import java.util.List;
import java.util.Map;

public class StoreNeo4j_II implements StoreInterface_II {

    private static final String URI = "neo4j://neo4j:7687";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "unodostres";
    private static final Driver driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));

    private void ensureDatabaseSchema(Session session) {
        session.writeTransaction(tx -> {
            // Crear restricción de unicidad para Document
            tx.run(
                    "CREATE CONSTRAINT IF NOT EXISTS FOR (d:Document) REQUIRE d.title IS UNIQUE"
            );
            // Crear restricción de unicidad para Word
            tx.run(
                    "CREATE CONSTRAINT IF NOT EXISTS FOR (w:Word) REQUIRE w.word IS UNIQUE"
            );
            return null;
        });
    }

    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        try (Session session = driver.session(SessionConfig.forDatabase("neo4j"))) {
            // Asegurar que el esquema esté configurado
            ensureDatabaseSchema(session);

            session.writeTransaction(tx -> {
                for (Map.Entry<String, Map<String, List<Integer>>> wordEntry : invertedIndex.entrySet()) {
                    String word = wordEntry.getKey();
                    Map<String, List<Integer>> docs = wordEntry.getValue();

                    for (Map.Entry<String, List<Integer>> docEntry : docs.entrySet()) {
                        String book = docEntry.getKey();
                        List<Integer> positions = docEntry.getValue();
                        int count = positions.size();

                        tx.run(
                                "MERGE (d:Document {title: $title}) " +
                                        "MERGE (w:Word {word: $word}) " +
                                        "MERGE (w)-[r:APPEARS_IN]->(d) " +
                                        "ON CREATE SET r.positions = $positions, w.count = $count " +
                                        "ON MATCH SET r.positions = COALESCE(r.positions, []) + $positions, w.count = w.count",
                                Map.of(
                                        "title", book,
                                        "word", word,
                                        "positions", positions,
                                        "count", count
                                )
                        );
                    }
                }
                return null;
            });
        } catch (Exception e) {
            System.err.println("Error storing inverted index in Neo4j: " + e.getMessage());
        }
    }
}