package org.main.metadata;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class StoreBinaryFile_MD implements StoreInterface_MD {
    private final String METADATA_PATH = "datamart/metadata.bin";

    @Override
    public void storeMetadata(Map<String, Map<String, String>> metadata) {
        String filePath = "";
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(metadata);

            System.out.println("Inverted index successfully stored in binary file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error storing inverted index to binary file: " + e.getMessage());
        }
    }
}