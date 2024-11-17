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
    private final static MongoClient mongoClient = new MongoClient("mongodb", 27017);
    private final static MongoDatabase database = mongoClient.getDatabase("book_search_engine");
    private static MongoCollection<Document> collection = null;

    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        collection = database.getCollection("invertedindex");
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        for (Map.Entry<String, Map<String, List<Integer>>> wordEntry : invertedIndex.entrySet()) {
            String word = wordEntry.getKey();
            Map<String, List<Integer>> books = wordEntry.getValue();

            for (Map.Entry<String, List<Integer>> bookEntry : books.entrySet()) {
                String book = bookEntry.getKey();
                List<Integer> positions = bookEntry.getValue();

                Document update = new Document("$addToSet", new Document("books." + book, new Document("$each", positions)));

                collection.updateOne(
                        new Document("word", word),
                        update,
                        new UpdateOptions().upsert(true)
                );
            }
        }
    }


}
