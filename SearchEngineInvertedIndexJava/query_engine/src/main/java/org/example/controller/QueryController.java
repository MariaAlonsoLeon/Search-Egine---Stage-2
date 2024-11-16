package org.example.controller;

import org.example.DataType;
import org.example.factory.SearchHelperFactory;
import org.example.helper.SearchHelper;

import static spark.Spark.*;

public class QueryController {

    public void registerRoutes() {
        // Endpoint do wyszukiwania słowa
        get("/search/:dataType/word/:word", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word = req.params(":word");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());  // Zamiana na enum
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchWord(word); // Zwrócenie wyniku
        });

        // Endpoint do wyszukiwania słowa w AND
        get("/search/:dataType/and/:word1/:word2", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word1 = req.params(":word1");
            String word2 = req.params(":word2");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchAnd(word1, word2); // Zwrócenie wyniku
        });

        // Endpoint do wyszukiwania słowa w OR
        get("/search/:dataType/or/:word1/:word2", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word1 = req.params(":word1");
            String word2 = req.params(":word2");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchOr(word1, word2); // Zwrócenie wyniku
        });

        // Endpoint do wyszukiwania słowa w NOT
        get("/search/:dataType/not/:word", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word = req.params(":word");
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.searchNot(word); // Zwrócenie wyniku
        });

        // Endpoint do paginacji
        get("/search/:dataType/paginate/:word/:pageSize/:pageNumber", (req, res) -> {
            String dataTypeParam = req.params(":dataType");
            String word = req.params(":word");
            int pageSize = Integer.parseInt(req.params(":pageSize"));
            int pageNumber = Integer.parseInt(req.params(":pageNumber"));
            DataType dataType = DataType.valueOf(dataTypeParam.toUpperCase());
            SearchHelper helper = SearchHelperFactory.getHelper(dataType);
            return helper.paginate(word, pageSize, pageNumber); // Zwrócenie wyniku
        });
    }
}
