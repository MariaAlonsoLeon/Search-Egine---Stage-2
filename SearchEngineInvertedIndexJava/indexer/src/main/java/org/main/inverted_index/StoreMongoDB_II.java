package org.main.inverted_index;

import java.util.Arrays;
import java.util.logging.Level;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoreMongoDB_II implements StoreInterface_II {
    private final static MongoClient mongoClient = new MongoClient("localhost", 27017);
    private final static MongoDatabase database = mongoClient.getDatabase("book_search_engine");
    private static MongoCollection<Document> collection = null;

    /*@Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        collection = database.getCollection("invertedindex");
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        // Loop through the inverted index for each book
        for (Map.Entry<String, Map<String, List<Integer>>> bookEntry : invertedIndex.entrySet()) {
            String word = bookEntry.getKey();  // The book name (e.g., "datalake/20241111/book_5.txt")
            Map<String, List<Integer>> words = bookEntry.getValue();  // Each word and its positions in this book

            // Loop over each word in the current book
            for (Map.Entry<String, List<Integer>> wordEntryInBook : words.entrySet()) {
                String book = wordEntryInBook.getKey();  // The word
                List<Integer> positions = wordEntryInBook.getValue();  // The positions of the word in the current book

                // Construct the update query to add the book and its positions for this word
                Document update = new Document("$addToSet", new Document("words." + word + "." + book, positions));

                // Update the MongoDB document, adding the book positions to the 'words' object
                collection.updateOne(
                        new Document("word", "all"),  // Use a common key like "all" for the word document
                        update,  // Add the new book and positions to the existing word
                        new UpdateOptions().upsert(true) // Create the document if it doesn't exist
                );
            }
        }
    }*/

    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        collection = database.getCollection("invertedindex");
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        // Loop through the inverted index for each word
        for (Map.Entry<String, Map<String, List<Integer>>> wordEntry : invertedIndex.entrySet()) {
            String word = wordEntry.getKey();  // The word (e.g., "example")
            Map<String, List<Integer>> books = wordEntry.getValue();  // Each book and positions of this word in the book

            // Loop over each book for the current word
            for (Map.Entry<String, List<Integer>> bookEntry : books.entrySet()) {
                String book = bookEntry.getKey();  // The book name (e.g., "book_5")
                List<Integer> positions = bookEntry.getValue();  // The positions of the word in the current book

                // Construct the update query to add positions for the word in the current book
                Document update = new Document("$addToSet", new Document("books." + book, new Document("$each", positions)));

                // Update the MongoDB document, adding or appending the positions for this word in the specific book
                collection.updateOne(
                        new Document("word", word),  // Filter by word
                        update,  // Add the book and its positions
                        new UpdateOptions().upsert(true) // Create the document if it doesn't exist
                );
            }
        }
    }


}
