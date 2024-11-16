package org.example.all;

import org.example.InvertedIndex.Binary.ContextSearchBinary;
import org.example.InvertedIndex.Binary.SingleWordSearchBinary;
import org.example.InvertedIndex.TXT.ContextSearchFile;
import org.example.Metadata.Binary.SearchByAuthorBinaryFile;
import org.example.Metadata.Binary.SearchByDateBinaryFile;
import org.example.Metadata.Binary.SearchByLanguageBinaryFile;
import org.example.Metadata.TXT.FetchMetadataByBookNameFile;
import org.example.Metadata.TXT.SearchByAuthorFile;
import org.example.Metadata.TXT.SearchByDateFile;
import org.example.Metadata.TXT.SearchByLanguageFile;
//import org.main.inverted_index.StoreInterface_II;
//import org.main.inverted_index.StoreNeo4j_II;
//import org.neo4j.driver.AuthTokens;
//import org.neo4j.driver.Driver;
//import org.neo4j.driver.GraphDatabase;
import org.example.InvertedIndex.TXT.SingleWordSearchFile;

import java.util.*;

public class QueryExample {
    public static void main(String[] args) {
        //Driver driver = GraphDatabase.driver("neo4j://localhost:7687", AuthTokens.basic("neo4j", "unodostres"));
        //StoreInterface_II store = new StoreNeo4j_II();
        //QueryEngine queryEngine = new Neo4jQueryEngine(store);

        // Búsqueda de una palabra
        //SearchCommand singleWordSearch = new SingleWordSearchNeo4J("near", driver);
        //List<String> singleWordResults = queryEngine.search(singleWordSearch);

        // Búsqueda de palabras en conjunción

        // Imprimir resultados
        //System.out.println("Resultados de búsqueda para 'amor': " + singleWordResults);

        //MongoDB_II mongo = new MongoDB_II();

        //System.out.println(mongo.searchWord("ten"));

        //SearchWordWithContextNeo4J single = new SearchWordWithContextNeo4J("prepare", driver, "datalake", 5);
        //SearchByAuthorNeo4J single = new SearchByAuthorNeo4J("John F. Kennedy", driver);
        //SearchByDate single = new SearchByDate("November 1, 1973 [eBook #3]", driver);
        //SearchByLanguage single = new SearchByLanguage("English", driver);
        //SingleWordSearchMongoDB single = new SingleWordSearchMongoDB("prepare");
        //ContextSearchMongo single = new ContextSearchMongo("prepare", 5, "datalake");
        //SearchByAuthorMongo single = new SearchByAuthorMongo("Thomas Jefferson");
        //SearchByDateMongo single = new SearchByDateMongo("December 1, 1971 [eBook #1]");
        //SearchByLanguageMongo single = new SearchByLanguageMongo("English");
        //FetchMetadataByBookNameMongo single = new FetchMetadataByBookNameMongo("datalake/20241111/book_1.txt");
        //FetchMetadataByBookNameNeo4j single = new FetchMetadataByBookNameNeo4j("datalake/20241111/book_1.txt");
        //System.out.println(single.execute());

        /*List<String> keywords = List.of("");  // Palabras clave
        String author = "Thomas Jefferson";  // Filtro por autor
        String startDate = "1900-01-01";  // Fecha de inicio
        String endDate = "2025-12-31";  // Fecha de fin
        String language = "English";  // Idioma

        // Ejecutar la búsqueda booleana
        BooleanSearchMongoDB search = new BooleanSearchMongoDB(keywords, author, startDate, endDate, language);
        System.out.println(search.execute());*/

        //SingleWordSearchBinary single = new SingleWordSearchBinary("prepare");
        //SingleWordSearchFile single = new SingleWordSearchFile("near", "datamart/inverted_index.txt");
        //ContextSearchFile single = new ContextSearchFile("English", 3, "datamart/inverted_index.txt", "datalake/");
        //SearchByAuthorFile single = new SearchByAuthorFile("United States", "datamart/metadata.txt");
        //SearchByDateFile single = new SearchByDateFile("1971-12-01", "datamart/metadata.txt");
        //SearchByLanguageFile single = new SearchByLanguageFile("English", "datamart/metadata.txt");
        //FetchMetadataByBookNameFile single = new FetchMetadataByBookNameFile("20241111/book_2", "datamart/metadata.txt");
        //SearchByAuthorBinaryFile single = new SearchByAuthorBinaryFile("Thomas Jefferson", "datamart/metadata.dat");
        //SearchByDateBinaryFile single = new SearchByDateBinaryFile("1971-12-01", "datamart/metadata.dat");
        //SearchByLanguageBinaryFile single = new SearchByLanguageBinaryFile("English", "datamart/metadata.dat");
        //SingleWordSearchBinary single = new SingleWordSearchBinary("English", "datamart/inverted_index.dat");
        //ContextSearchBinary single = new ContextSearchBinary("English", 3, "datamart/inverted_index.dat", "datalake");
        //System.out.println(single.execute());
    }
}