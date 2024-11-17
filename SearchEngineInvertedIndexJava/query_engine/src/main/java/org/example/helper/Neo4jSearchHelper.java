package org.example.helper;

import org.example.InvertedIndex.Neo4J.SearchWordWithContextNeo4J;
import org.example.InvertedIndex.Neo4J.SingleWordSearchNeo4j;
import org.example.Metadata.Neo4J.FetchMetadataByBookNameNeo4j;
import org.example.Metadata.Neo4J.SearchByAuthorNeo4J;
import org.example.Metadata.Neo4J.SearchByDateNeo4J;
import org.example.Metadata.Neo4J.SearchByLanguageNeo4J;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.util.List;
import java.util.Map;

public class Neo4jSearchHelper implements SearchHelper {
    private static Driver driver = GraphDatabase.driver("bolt://neo4j:7687", AuthTokens.basic("neo4j", "unodostres"));

    @Override
    public Map<String, List<String>> searchSingleWordII(String word) {
        SingleWordSearchNeo4j single = new SingleWordSearchNeo4j(word);
        return single.execute();
    }

    @Override
    public Map<String, List<String>> searchWithContextII(String word, int contextSize, String documentFolderPath) {
        SearchWordWithContextNeo4J contextNeo4J = new SearchWordWithContextNeo4J(word, driver, "datalake/", contextSize);
        return contextNeo4J.execute();
    }

    @Override
    public List<String> searchByAuthorMD(String author) {
        SearchByAuthorNeo4J searchByAuthorNeo4J = new SearchByAuthorNeo4J(author, driver);
        return searchByAuthorNeo4J.execute();
    }

    @Override
    public List<String> searchByDateMD(String date) {
        SearchByDateNeo4J searchByDateNeo4J = new SearchByDateNeo4J(date, driver);
        return searchByDateNeo4J.execute();
    }

    @Override
    public List<String> searchByLanguageMD(String language) {
        SearchByLanguageNeo4J searchByLanguageNeo4J =new SearchByLanguageNeo4J(language, driver);
        return searchByLanguageNeo4J.execute();
    }

    @Override
    public Map<String, String> fetchMD(String bookName) {
        FetchMetadataByBookNameNeo4j fetchMetadataByBookNameNeo4j = new FetchMetadataByBookNameNeo4j(bookName);
        return fetchMetadataByBookNameNeo4j.execute();
    }
}
