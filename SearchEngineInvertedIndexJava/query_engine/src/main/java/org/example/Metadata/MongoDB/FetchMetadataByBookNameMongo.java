package org.example.Metadata.MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.Metadata.FetchMetadataCommand;

import java.util.HashMap;
import java.util.Map;

public class FetchMetadataByBookNameMongo implements FetchMetadataCommand {
    private final String bookName;
    private final MongoClient mongoClient;

    public FetchMetadataByBookNameMongo(String bookName) {
        this.bookName = bookName;
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
    }

    @Override
    public Map<String, String> execute() {
        Map<String, String> metadata = new HashMap<>();

        try {
            MongoDatabase database = mongoClient.getDatabase("book_search_engine");
            MongoCollection<Document> collection = database.getCollection("metadata");

            // Consulta para obtener el documento específico según su nombre
            Document query = new Document("documentName", bookName);
            Document doc = collection.find(query).first();

            if (doc != null) {
                String author = doc.getString("author");
                String date = doc.getString("date");
                String language = doc.getString("language");

                // Agregar metadatos al mapa de resultados
                if (author != null) metadata.put("author", author);
                if (date != null) metadata.put("date", date);
                if (language != null) metadata.put("language", language);
            }
        } catch (Exception e) {
            System.err.println("Error fetching metadata for book: " + e.getMessage());
        } finally {
            mongoClient.close();
        }

        return metadata;
    }
}