package org.example.Metadata.Neo4J;

import org.example.Metadata.SearchMetadataCommand;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchByAuthorNeo4J implements SearchMetadataCommand {
    private final String author;
    private final Driver driver;

    public SearchByAuthorNeo4J(String author, Driver driver) {
        this.author = author;
        this.driver = driver;
    }

    @Override
    public List<String> execute() {
        List<String> results = new ArrayList<>();

        try (Session session = driver.session(SessionConfig.forDatabase("metadata"))) {
            String query = """
                    MATCH (doc:File {author: $author})
                    RETURN doc.name AS name
                    """;
            var result = session.run(query, Map.of("author", author));

            while (result.hasNext()) {
                Record record = result.next();
                // Agregar solo el título del libro a la lista de resultados
                System.out.println();
                results.add(record.get("name").asString());
            }
        } catch (Exception e) {
            System.err.println("Error executing author search: " + e.getMessage());
        }

        return results;
    }
}