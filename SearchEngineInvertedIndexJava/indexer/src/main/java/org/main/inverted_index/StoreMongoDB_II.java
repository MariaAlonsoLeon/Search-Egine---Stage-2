package org.main.inverted_index;

import java.util.logging.Level;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoreMongoDB_II implements StoreInterface_II{
    private final static MongoClient mongoClient = new MongoClient("localhost", 27017);
    private final static MongoDatabase database = mongoClient.getDatabase("book_search_engine");
    private static MongoCollection<Document> collection = null;

    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        collection = database.getCollection("invertedindex");
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        for (Map.Entry<String, Map<String, List<Integer>>> documentEntry : invertedIndex.entrySet()) {
            String bookName = documentEntry.getKey();
            Map<String, List<Integer>> invertedIndexBody = documentEntry.getValue();

            Document doc = new Document("documentName", bookName);

            Map<String, Object> indexMap = new HashMap<>();
            for (Map.Entry<String, List<Integer>> wordEntry : invertedIndexBody.entrySet()) {
                indexMap.put(wordEntry.getKey(), wordEntry.getValue());
            }

            doc.append("invertedIndex", indexMap);
            collection.insertOne(doc);
        }

        //System.out.println("Inverted Index stored in MongoDB.");
    }
}
