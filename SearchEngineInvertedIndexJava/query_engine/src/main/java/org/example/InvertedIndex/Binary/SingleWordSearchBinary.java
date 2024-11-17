package org.example.InvertedIndex.Binary;

import org.example.InvertedIndex.SearchCommand;

import java.io.*;
import java.util.*;

public class SingleWordSearchBinary implements SearchCommand {
    private final String word;
    private final String filePath;

    public SingleWordSearchBinary(String word, String filePath) {
        this.word = word.toLowerCase();
        this.filePath = filePath;
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> results = new HashMap<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            while (dis.available() > 0) {
                byte fieldCode = dis.readByte();
                if (fieldCode != 1) {
                    dis.readUTF();
                    continue;
                }
                String currentWord = dis.readUTF();

                if (!currentWord.equals(word)) {
                    skipDocumentEntries(dis);
                    continue;
                }

                while (dis.available() > 0) {
                    fieldCode = dis.readByte();
                    if (fieldCode == 0) {
                        break;
                    }

                    if (fieldCode == 2) {
                        String documentName = dis.readUTF();
                        fieldCode = dis.readByte();

                        if (fieldCode == 3) {
                            int positionsSize = dis.readInt();
                            List<String> positionList = new ArrayList<>();

                            for (int i = 0; i < positionsSize; i++) {
                                int position = dis.readInt();
                                positionList.add(String.valueOf(position));
                            }

                            results.putIfAbsent(documentName, new ArrayList<>());
                            results.get(documentName).add(String.join(", ", positionList));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the binary inverted index file: " + e.getMessage());
        }

        return results;
    }

    private void skipDocumentEntries(DataInputStream dis) throws IOException {
        byte fieldCode;
        while (dis.available() > 0) {
            fieldCode = dis.readByte();
            if (fieldCode == 0) {
                break;
            }
            if (fieldCode == 2) {
                dis.readUTF();
                fieldCode = dis.readByte();
                if (fieldCode == 3) {
                    int positionsSize = dis.readInt();
                    for (int i = 0; i < positionsSize; i++) {
                        dis.readInt();
                    }
                }
            }
        }
    }
}