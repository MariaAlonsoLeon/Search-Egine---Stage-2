package org.example.InvertedIndex.MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import org.example.InvertedIndex.SearchCommand;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.*;

public class ContextSearchMongo implements SearchCommand {
    private final String word;
    private final int contextSize;
    private final String documentFolderPath; // Ruta de la carpeta con los archivos de los documentos
    private final MongoClient mongoClient;

    public ContextSearchMongo(String word, int contextSize, String documentFolderPath) {
        this.word = word.toLowerCase();
        this.contextSize = contextSize;
        this.documentFolderPath = documentFolderPath;
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> contexts = new HashMap<>();

        try {
            MongoDatabase database = mongoClient.getDatabase("book_search_engine");
            MongoCollection<Document> collection = database.getCollection("invertedindex");

            // Realizar la consulta para obtener las posiciones de la palabra en cada documento
            Document query = new Document("word", word);
            FindIterable<Document> documents = collection.find(query);

            for (Document doc : documents) {
                Document books = (Document) doc.get("books");
                for (String bookTitle : books.keySet()) {
                    List<Integer> positions = (List<Integer>) books.get(bookTitle);
                    List<String> bookContexts = new ArrayList<>();

                    // Leer el archivo de texto completo del libro para obtener el contexto
                    File bookFile = new File(documentFolderPath, bookTitle + ".txt");
                    StringBuilder bookContent = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            bookContent.append(line).append(" ");
                        }
                    }

                    String bookText = bookContent.toString().toLowerCase();
                    String[] words = bookText.split("\\W+");


                    // Generar el contexto alrededor de cada posiciÃ³n de la palabra
                    for (Integer pos : positions) {
                        int start = Math.max(0, pos - contextSize);
                        int end = Math.min(words.length, pos + contextSize + word.length());
                        String context = String.join(" ", Arrays.asList(words).subList(start, end));
                        bookContexts.add(context);
                    }

                    contexts.put(bookTitle, bookContexts);
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing contextual word search in MongoDB: " + e.getMessage());
        } finally {
            mongoClient.close();
        }

        return contexts;
    }
}
