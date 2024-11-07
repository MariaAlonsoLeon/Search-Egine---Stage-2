package org.main.inverted_index;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreMongoDB_II implements StoreInterface_II{
    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("book_search_engine");
        MongoCollection<Document> collection = database.getCollection("invertedindex");

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

        System.out.println("Inverted Index stored in MongoDB.");
        mongoClient.close();
    }
}