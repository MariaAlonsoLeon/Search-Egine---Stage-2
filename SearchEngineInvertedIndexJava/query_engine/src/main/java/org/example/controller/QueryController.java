package org.example.controller;

import org.example.DataType;
import org.example.factory.SearchHelperFactory;
import org.example.helper.SearchHelper;

import static spark.Spark.*;

public class QueryController {

    public void registerRoutes() {
        // (searchSingleWordII)
        get("/search/:dataType/single-word/:word", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word = req.params(":word");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchSingleWordII(word);
        });

        // (searchWithContextII)
        get("/search/:dataType/with-context/:word/:contextSize/:documentFolderPath", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word = req.params(":word");
            int contextSize = Integer.parseInt(req.params(":contextSize"));
            String documentFolderPath = req.params(":documentFolderPath");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchWithContextII(word, contextSize, documentFolderPath);
        });

        // (searchByAuthorMD)
        get("/search/:dataType/author/:author", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String author = req.params(":author");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchByAuthorMD(author);
        });

        // (searchByDateMD)
        get("/search/:dataType/date/:date", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String date = req.params(":date");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchByDateMD(date);
        });

        // (searchByLanguageMD)
        get("/search/:dataType/language/:language", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String language = req.params(":language");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchByLanguageMD(language);
        });

        // (fetchMD)
        get("/search/:dataType/book/:bookName", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String bookName = req.params(":bookName");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.fetchMD(bookName);
        });
    }
}
