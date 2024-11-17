package org.example.Metadata.Neo4J;

import org.example.Metadata.SearchMetadataCommand;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchByLanguageNeo4J implements SearchMetadataCommand {
    private final String language;
    private final Driver driver;

    public SearchByLanguageNeo4J(String language, Driver driver) {
        this.language = language;
        this.driver = driver;
    }

    @Override
    public List<String> execute() {
        List<String> results = new ArrayList<>();

        try (Session session = driver.session(SessionConfig.forDatabase("metadata"))) {
            String query = """
                    MATCH (doc:File {language: $language})
                    RETURN doc.name AS name
                    """;
            var result = session.run(query, Map.of("language", language));

            while (result.hasNext()) {
                Record record = result.next();
                results.add(record.get("name").asString());
            }
        } catch (Exception e) {
            System.err.println("Error executing language search: " + e.getMessage());
        }

        return results;
    }
}