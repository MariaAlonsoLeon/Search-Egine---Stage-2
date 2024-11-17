package org.main.metadata;

import org.neo4j.driver.*;

import java.util.Map;

public class StoreNeo4j_MD implements StoreInterface_MD {
    private static final String URI = "neo4j://neo4j:7687";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "unodostres";
    private static final Driver driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));

    // Método para asegurar el esquema de la base de datos
    private void ensureDatabaseSchema(Session session) {
        session.writeTransaction(tx -> {
            // Crear restricción de unicidad para File
            tx.run(
                    "CREATE CONSTRAINT IF NOT EXISTS FOR (f:File) REQUIRE f.name IS UNIQUE"
            );
            return null;
        });
    }

    @Override
    public void storeMetadata(Map<String, Map<String, String>> metadata) {
        try (Session session = driver.session(SessionConfig.forDatabase("neo4j"))) {
            // Asegurar que el esquema esté configurado
            ensureDatabaseSchema(session);

            // Almacenar los metadatos
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