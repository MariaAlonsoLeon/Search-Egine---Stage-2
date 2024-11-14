package org.main.metadata;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StoreBinary_MD implements StoreInterface_MD {
    private final String filePath = "datamart/metadata.dat";

    // Mapa para asociar abreviaturas de campos
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

        // Crear directorio padre si no existe
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Escribir los metadatos en formato binario
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true))) { // true para modo "append"
            for (Map.Entry<String, Map<String, String>> documentEntry : metadata.entrySet()) {
                String bookName = documentEntry.getKey();
                Map<String, String> metadataBody = documentEntry.getValue();

                // Escribir la clave de "documentName" y el valor
                dos.writeByte(metadataKeys.get("documentName"));
                dos.writeUTF(bookName);

                // Escribir cada metadato con su clave abreviada
                for (Map.Entry<String, String> entry : metadataBody.entrySet()) {
                    Byte keyByte = metadataKeys.get(entry.getKey());
                    if (keyByte != null) {
                        dos.writeByte(keyByte);       // Escribir clave en binario
                        dos.writeUTF(entry.getValue()); // Escribir valor en binario
                    }
                }
                dos.writeByte(0); // SeÃ±al de fin de registro
            }
            System.out.println("Metadata stored successfully in binary file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing metadata to binary file: " + e.getMessage());
        }
    }
}