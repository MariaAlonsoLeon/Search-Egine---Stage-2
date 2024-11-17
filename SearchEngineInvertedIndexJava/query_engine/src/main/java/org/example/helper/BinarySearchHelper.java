package org.example.helper;

import org.example.InvertedIndex.Binary.ContextSearchBinary;
import org.example.InvertedIndex.Binary.SingleWordSearchBinary;
import org.example.InvertedIndex.TXT.ContextSearchFile;
import org.example.Metadata.Binary.FetchMetadataByBinaryFile;
import org.example.Metadata.Binary.SearchByAuthorBinaryFile;
import org.example.Metadata.Binary.SearchByDateBinaryFile;
import org.example.Metadata.Binary.SearchByLanguageBinaryFile;
import java.util.List;
import java.util.Map;

public class BinarySearchHelper implements SearchHelper {
    @Override
    public Map<String, List<String>> searchSingleWordII(String word) {
        SingleWordSearchBinary single = new SingleWordSearchBinary(word, "datamart/inverted_index.dat");
        return single.execute();
    }

    @Override
    public Map<String, List<String>> searchWithContextII(String word, int contextSize, String documentFolderPath) {
        ContextSearchBinary context = new ContextSearchBinary(word, 5, "datamart/inverted_index.dat", "datalake/");
        return context.execute();
    }

    @Override
    public List<String> searchByAuthorMD(String author) {
        SearchByAuthorBinaryFile authorSearch = new SearchByAuthorBinaryFile(author, "datamart/metadata.dat");
        return authorSearch.execute();
    }

    @Override
    public List<String> searchByDateMD(String date) {
        SearchByDateBinaryFile dateSearch = new SearchByDateBinaryFile(date, "datamart/metadata.dat");
        return dateSearch.execute();
    }

    @Override
    public List<String> searchByLanguageMD(String language) {
        SearchByLanguageBinaryFile languageSearch = new SearchByLanguageBinaryFile(language, "datamart/metadata.dat");
        return languageSearch.execute();
    }

    @Override
    public Map<String, String> fetchMD(String bookName) {
        FetchMetadataByBinaryFile fetchMetadataByBinaryFile = new FetchMetadataByBinaryFile(bookName, "datamart/metadata.dat");
        return fetchMetadataByBinaryFile.execute();
    }
}
