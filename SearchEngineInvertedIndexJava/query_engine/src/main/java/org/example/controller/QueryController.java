package org.example.controller;

import com.google.gson.Gson;
import org.example.DataType;
import org.example.factory.SearchHelperFactory;
import org.example.helper.SearchHelper;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class QueryController {

    public void registerRoutes() {
        Gson gson = new Gson();
        get("/search/:dataType/single-word/:word", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word = req.params(":word");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            Map<String, List<String>> result = helper.searchSingleWordII(word);
            return gson.toJson(result);
        });

        get("/search/:dataType/with-context/:word/:contextSize/:documentFolderPath", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word = req.params(":word");
            int contextSize = Integer.parseInt(req.params(":contextSize"));
            String documentFolderPath = req.params(":documentFolderPath");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            Map<String, List<String>> result = helper.searchWithContextII(word, contextSize, documentFolderPath);
            return gson.toJson(result);
        });

        get("/search/:dataType/author/:author", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String author = req.params(":author");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            List<String> result = helper.searchByAuthorMD(author);
            return gson.toJson(result);
        });

        get("/search/:dataType/date/:date", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String date = req.params(":date");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            List<String> result = helper.searchByDateMD(date);
            return gson.toJson(result);
        });

        get("/search/:dataType/language/:language", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String language = req.params(":language");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            List<String> result = helper.searchByLanguageMD(language);
            return gson.toJson(result);
        });

        get("/search/:dataType/book/:bookName", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String bookName = req.params(":bookName");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            Map<String, String> result = helper.fetchMD(bookName);
            return gson.toJson(result);
        });
    }
}
