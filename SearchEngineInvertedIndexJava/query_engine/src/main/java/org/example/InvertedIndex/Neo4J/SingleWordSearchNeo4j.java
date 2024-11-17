package org.example.InvertedIndex.Neo4J;

import org.example.InvertedIndex.SearchCommand;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

public class SingleWordSearchNeo4j implements SearchCommand {
    private final String word;
    private final Driver neo4jDriver;

    public SingleWordSearchNeo4j(String word) {
        this.word = word;
        this.neo4jDriver = GraphDatabase.driver("neo4j://localhost:7687", AuthTokens.basic("neo4j", "unodostres"));
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> results = new HashMap<>();

        try (Session session = neo4jDriver.session(SessionConfig.forDatabase("invertedindex"))) {
            String query = """
                    MATCH (w:Word {word: $word})-[r:APPEARS_IN]->(d:Document)
                    RETURN d.title AS document, r.positions AS positions""";
            var result = session.run(query, Map.of("word", word));

            while (result.hasNext()) {
                var record = result.next();
                String title = record.get("document").asString();
                List<Integer> positions = record.get("positions").asList(Value::asInt);
                System.out.println(title);
                System.out.println(positions);

                List<String> positionStrings = new ArrayList<>();
                for (Integer pos : positions) {
                    positionStrings.add(pos.toString());
                }

                results.put(title, positionStrings);
            }
        } catch (Exception e) {
            System.err.println("Error executing single word search in Neo4j: " + e.getMessage());
        } finally {
            neo4jDriver.close();
        }

        return results;
    }
}