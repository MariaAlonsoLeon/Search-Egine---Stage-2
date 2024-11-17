package org.example.helper;

import org.example.InvertedIndex.TXT.ContextSearchFile;
import org.example.InvertedIndex.TXT.SingleWordSearchFile;
import org.example.Metadata.TXT.FetchMetadataByBookNameFile;
import org.example.Metadata.TXT.SearchByAuthorFile;
import org.example.Metadata.TXT.SearchByDateFile;
import org.example.Metadata.TXT.SearchByLanguageFile;
import java.util.List;
import java.util.Map;

public class JsonSearchHelper implements SearchHelper {
    @Override
    public Map<String, List<String>> searchSingleWordII(String word) {
        SingleWordSearchFile single = new SingleWordSearchFile(word, "datamart/inverted_index.txt");
        return single.execute();
    }

    @Override
    public Map<String, List<String>> searchWithContextII(String word, int contextSize, String documentFolderPath) {
        ContextSearchFile context = new ContextSearchFile(word, 5, "datamart/inverted_index.txt", "datalake/");
        return context.execute();
    }

    @Override
    public List<String> searchByAuthorMD(String author) {
        SearchByAuthorFile authorFile = new SearchByAuthorFile(author, "datamart/metadata.txt");
        return authorFile.execute();
    }

    @Override
    public List<String> searchByDateMD(String date) {
        SearchByDateFile dateSearch = new SearchByDateFile(date, "datamart/metadata.txt");
        return dateSearch.execute();
    }

    @Override
    public List<String> searchByLanguageMD(String language) {
        SearchByLanguageFile languageSearch = new SearchByLanguageFile(language, "datamart/metadata.txt");
        return languageSearch.execute();
    }

    @Override
    public Map<String, String> fetchMD(String bookName) {
        FetchMetadataByBookNameFile fetchMetadata = new FetchMetadataByBookNameFile(bookName, "datamart/metadata.txt");
        return fetchMetadata.execute();
    }
}
