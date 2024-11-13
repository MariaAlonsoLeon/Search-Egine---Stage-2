package org.example.query_engine.controller;

import org.example.query_engine.DataType;
import org.example.query_engine.factory.SearchHelperFactory;
import org.example.query_engine.helper.SearchHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*")
public class QueryController {

    @GetMapping("/{dataType}/word/{word}")
    public String searchWord(@PathVariable DataType dataType, @PathVariable String word) {
        SearchHelper helper = SearchHelperFactory.getHelper(dataType);
        return helper.searchWord(word);
    }

    @GetMapping("/{dataType}/and/{word1}/{word2}")
    public String searchAnd(@PathVariable DataType dataType, @PathVariable String word1, @PathVariable String word2) {
        SearchHelper helper = SearchHelperFactory.getHelper(dataType);
        return helper.searchAnd(word1, word2);
    }

    @GetMapping("/{dataType}/or/{word1}/{word2}")
    public String searchOr(@PathVariable DataType dataType, @PathVariable String word1, @PathVariable String word2) {
        SearchHelper helper = SearchHelperFactory.getHelper(dataType);
        return helper.searchOr(word1, word2);
    }

    @GetMapping("/{dataType}/not/{word}")
    public String searchNot(@PathVariable DataType dataType, @PathVariable String word) {
        SearchHelper helper = SearchHelperFactory.getHelper(dataType);
        return helper.searchNot(word);
    }

    @GetMapping("/{dataType}/paginate/{word}/{pageSize}/{pageNumber}")
    public String paginate(@PathVariable DataType dataType, @PathVariable String word,
                           @PathVariable int pageSize, @PathVariable int pageNumber) {
        SearchHelper helper = SearchHelperFactory.getHelper(dataType);
        return helper.paginate(word, pageSize, pageNumber);
    }
}
