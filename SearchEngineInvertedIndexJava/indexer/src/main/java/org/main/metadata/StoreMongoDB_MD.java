package org.main.metadata;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Map;

public class StoreMongoDB_MD implements StoreInterface_MD {
    @Override
    public void storeMetadata(Map<String, Map<String, String>> metadata) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("book_search_engine");
        MongoCollection<Document> collection = database.getCollection("metadata");

        for (Map.Entry<String, Map<String, String>> documentEntry : metadata.entrySet()) {
            String bookName = documentEntry.getKey();
            Map<String, String> metadataBody = documentEntry.getValue();

            Document doc = new Document("documentName", bookName);

            for (Map.Entry<String, String> wordEntry : metadataBody.entrySet()) {
                doc.append(wordEntry.getKey(), wordEntry.getValue());
            }

            collection.insertOne(doc);
        }

        System.out.println("Metadata stored in MongoDB.");
        mongoClient.close();
    }
}