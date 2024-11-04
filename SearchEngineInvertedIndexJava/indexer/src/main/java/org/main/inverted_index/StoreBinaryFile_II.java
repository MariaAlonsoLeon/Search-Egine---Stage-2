package org.main.inverted_index;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class StoreBinaryFile_II implements StoreInterface_II{
    private final String INVERTED_INDEX_PATH = "datamart/inverted_index.bin";
    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        try (FileOutputStream fileOut = new FileOutputStream(INVERTED_INDEX_PATH);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(invertedIndex);

            System.out.println("Inverted index successfully stored in binary file: " + INVERTED_INDEX_PATH);
        } catch (IOException e) {
            System.err.println("Error storing inverted index to binary file: " + e.getMessage());
        }
    }

}