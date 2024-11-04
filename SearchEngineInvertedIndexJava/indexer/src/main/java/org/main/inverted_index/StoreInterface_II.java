package org.main.inverted_index;

import java.util.List;
import java.util.Map;

public interface StoreInterface_II {
    void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex);
}
