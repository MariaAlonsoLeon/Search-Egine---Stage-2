package org.example.InvertedIndex;

import java.util.List;
import java.util.Map;

public interface SearchCommand {
    Map<String, List<String>> execute();
}