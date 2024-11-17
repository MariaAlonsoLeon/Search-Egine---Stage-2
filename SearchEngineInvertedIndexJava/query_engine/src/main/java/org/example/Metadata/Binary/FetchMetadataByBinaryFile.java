package org.example.Metadata.Binary;

import org.example.Metadata.FetchMetadataCommand;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FetchMetadataByBinaryFile implements FetchMetadataCommand {
    private final String bookName;
    private final String filePath;

    private static final Map<Byte, String> metadataKeys = new HashMap<>();

    static {
        metadataKeys.put((byte) 1, "documentName");
        metadataKeys.put((byte) 2, "author");
        metadataKeys.put((byte) 3, "date");
        metadataKeys.put((byte) 4, "language");
    }

    public FetchMetadataByBinaryFile(String bookName, String filePath) {
        this.bookName = bookName.toLowerCase();
        this.filePath = filePath;
    }

    @Override
    public Map<String, String> execute() {
        Map<String, String> metadata = new HashMap<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            while (dis.available() > 0) {
                Map<String, String> currentMetadata = new HashMap<>();
                String currentDocumentName = null;

                while (true) {
                    byte key = dis.readByte();
                    if (key == 0) break;

                    String keyName = metadataKeys.get(key);
                    String value = dis.readUTF();

                    if ("documentName".equals(keyName)) {
                        currentDocumentName = value.toLowerCase();
                    } else if (keyName != null) {
                        currentMetadata.put(keyName, value);
                    }
                }

                if (bookName.equals(currentDocumentName)) {
                    metadata = currentMetadata;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the metadata file: " + e.getMessage());
        }

        return metadata;
    }
}
