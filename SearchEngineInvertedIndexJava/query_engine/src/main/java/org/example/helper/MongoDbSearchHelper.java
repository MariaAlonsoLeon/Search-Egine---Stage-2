package org.example.helper;

import org.example.InvertedIndex.MongoDB.ContextSearchMongo;
import org.example.InvertedIndex.MongoDB.SingleWordSearchMongoDB;
import org.example.Metadata.MongoDB.FetchMetadataByBookNameMongo;
import org.example.Metadata.MongoDB.SearchByAuthorMongo;
import org.example.Metadata.MongoDB.SearchByDateMongo;
import org.example.Metadata.MongoDB.SearchByLanguageMongo;
import java.util.List;
import java.util.Map;

public class MongoDbSearchHelper implements SearchHelper {
    @Override
    public Map<String, List<String>> searchSingleWordII(String word) {
        SingleWordSearchMongoDB single = new SingleWordSearchMongoDB(word);
        return single.execute();
    }

    @Override
    public Map<String, List<String>> searchWithContextII(String word, int contextSize, String documentFolderPath) {
        ContextSearchMongo context = new ContextSearchMongo(word, contextSize, documentFolderPath);
        return context.execute();
    }

    @Override
    public List<String> searchByAuthorMD(String author) {
        SearchByAuthorMongo authorMongo = new SearchByAuthorMongo(author);
        return authorMongo.execute();
    }

    @Override
    public List<String> searchByDateMD(String date) {
        SearchByDateMongo dateMongo = new SearchByDateMongo(date);
        return dateMongo.execute();
    }

    @Override
    public List<String> searchByLanguageMD(String language) {
        SearchByLanguageMongo languageMongo = new SearchByLanguageMongo(language);
        return languageMongo.execute();
    }

    @Override
    public Map<String, String> fetchMD(String bookName) {
        FetchMetadataByBookNameMongo fetchMetadataByBookNameMongo = new FetchMetadataByBookNameMongo(bookName);
        return fetchMetadataByBookNameMongo.execute();
    }
}
