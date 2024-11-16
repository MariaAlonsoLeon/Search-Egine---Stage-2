package org.example.Metadata.MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import org.example.Metadata.SearchMetadataCommand;

import java.util.ArrayList;
import java.util.List;

public class SearchByAuthorMongo implements SearchMetadataCommand {
    private final String author;
    private final MongoClient mongoClient;

    public SearchByAuthorMongo(String author) {
        this.author = author;
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
    }

    @Override
    public List<String> execute() {
        List<String> documentNames = new ArrayList<>();

        try {
            MongoDatabase database = mongoClient.getDatabase("book_search_engine");
            MongoCollection<Document> collection = database.getCollection("metadata");

            // Query to find all documents by the specified author
            Document query = new Document("author", author);
            FindIterable<Document> documents = collection.find(query);

            // Extract document names from each result
            for (Document doc : documents) {
                String documentName = doc.getString("documentName");
                if (documentName != null) {
                    documentNames.add(documentName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing author search in MongoDB: " + e.getMessage());
        } finally {
            mongoClient.close();
        }

        return documentNames;
    }
}