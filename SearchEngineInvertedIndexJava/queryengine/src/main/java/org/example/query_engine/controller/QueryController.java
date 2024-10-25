package org.example.query_engine.controller;

import org.example.query_engine.helper.InvertedIndexHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*")
public class QueryController {

    private final InvertedIndexHelper helper = new InvertedIndexHelper();

    @GetMapping("/{word}")
    public String searchWord(@PathVariable String word) {
        return helper.searchWord(word);
    }

    @GetMapping("/and/{word1}/{word2}")
    public String searchAnd(@PathVariable String word1, @PathVariable String word2) {
        return helper.searchAnd(word1, word2);
    }

    @GetMapping("/or/{word1}/{word2}")
    public String searchOr(@PathVariable String word1, @PathVariable String word2) {
        return helper.searchOr(word1, word2);
    }

    @GetMapping("/not/{word}")
    public String searchNot(@PathVariable String word) {
        return helper.searchNot(word);
    }

    @GetMapping("/paginate/{word}/{pageSize}/{pageNumber}")
    public String paginate(@PathVariable String word, @PathVariable int pageSize, @PathVariable int pageNumber) {
        return helper.paginate(word, pageSize, pageNumber);
    }
}
