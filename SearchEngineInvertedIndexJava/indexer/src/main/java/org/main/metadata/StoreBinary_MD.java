package org.main.metadata;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StoreBinary_MD implements StoreInterface_MD {
    private final String filePath = "datamart/metadata.dat";

    private static final Map<String, Byte> metadataKeys = new HashMap<>();
    static {
        metadataKeys.put("documentName", (byte) 1);
        metadataKeys.put("author", (byte) 2);
        metadataKeys.put("date", (byte) 3);
        metadataKeys.put("language", (byte) 4);
    }

    @Override
    public void storeMetadata(Map<String, Map<String, String>> metadata) {
        File file = new File(filePath);

        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true))) {
            for (Map.Entry<String, Map<String, String>> documentEntry : metadata.entrySet()) {
                String bookName = documentEntry.getKey();
                Map<String, String> metadataBody = documentEntry.getValue();

                dos.writeByte(metadataKeys.get("documentName"));
                dos.writeUTF(bookName);

                for (Map.Entry<String, String> entry : metadataBody.entrySet()) {
                    Byte keyByte = metadataKeys.get(entry.getKey());
                    if (keyByte != null) {
                        dos.writeByte(keyByte);
                        dos.writeUTF(entry.getValue());
                    }
                }
                dos.writeByte(0);
            }
            System.out.println("Metadata stored successfully in binary file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing metadata to binary file: " + e.getMessage());
        }
    }
}