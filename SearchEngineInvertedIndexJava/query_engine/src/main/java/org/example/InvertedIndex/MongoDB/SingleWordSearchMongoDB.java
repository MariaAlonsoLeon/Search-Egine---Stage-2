package org.example.InvertedIndex.MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import org.example.InvertedIndex.SearchCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleWordSearchMongoDB implements SearchCommand {
    private final String word;
    private final MongoClient mongoClient;

    public SingleWordSearchMongoDB(String word) {
        this.word = word;
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> results = new HashMap<>();

        try {
            MongoDatabase database = mongoClient.getDatabase("book_search_engine");
            MongoCollection<Document> collection = database.getCollection("invertedindex");

            Document query = new Document("word", word);
            FindIterable<Document> documents = collection.find(query);

            for (Document doc : documents) {
                Document books = (Document) doc.get("books");
                System.out.println(books);
                for (String bookTitle : books.keySet()) {
                    List<Integer> positions = (List<Integer>) books.get(bookTitle);

                    List<String> positionStrings = new ArrayList<>();
                    for (Integer pos : positions) {
                        positionStrings.add(pos.toString());
                    }

                    results.put(bookTitle, positionStrings);
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing single word search in MongoDB: " + e.getMessage());
        } finally {
            mongoClient.close();
        }

        return results;
    }
}
