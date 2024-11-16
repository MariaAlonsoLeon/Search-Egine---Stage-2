package org.example.Metadata.Neo4J;

import org.example.Metadata.SearchMetadataCommand;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchByDateNeo4J implements SearchMetadataCommand {
    private final String date;
    private final Driver driver;

    public SearchByDateNeo4J(String date, Driver driver) {
        this.date = date;
        this.driver = driver;
    }

    @Override
    public List<String> execute() {
        List<String> results = new ArrayList<>();

        try (Session session = driver.session(SessionConfig.forDatabase("metadata"))) {
            String query = """
                    MATCH (doc:File {date: $date})
                    RETURN doc.name AS name
                    """;
            var result = session.run(query, Map.of("date", date));

            while (result.hasNext()) {
                Record record = result.next();
                results.add(record.get("name").asString());
            }
        } catch (Exception e) {
            System.err.println("Error executing date search: " + e.getMessage());
        }

        return results;
    }
}