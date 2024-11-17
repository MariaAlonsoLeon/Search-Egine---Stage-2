package org.example.InvertedIndex.Binary;

import org.example.InvertedIndex.SearchCommand;

import java.io.*;
import java.util.*;

public class ContextSearchBinary implements SearchCommand {
    private final String word;
    private final int contextSize;
    private final String filePath;
    private final String documentFolderPath;

    public ContextSearchBinary(String word, int contextSize, String filePath, String documentFolderPath) {
        this.word = word.toLowerCase();
        this.contextSize = contextSize;
        this.filePath = filePath;
        this.documentFolderPath = documentFolderPath;
    }

    @Override
    public Map<String, List<String>> execute() {
        Map<String, List<String>> contexts = new HashMap<>();

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
                            List<Integer> positions = new ArrayList<>();
                            for (int i = 0; i < positionsSize; i++) {
                                positions.add(dis.readInt());
                            }

                            File bookFile = new File(documentFolderPath, documentName + ".txt");
                            StringBuilder bookContent = new StringBuilder();

                            try (BufferedReader bookReader = new BufferedReader(new FileReader(bookFile))) {
                                String lineContent;
                                while ((lineContent = bookReader.readLine()) != null) {
                                    bookContent.append(lineContent).append(" ");
                                }
                            }

                            String bookText = bookContent.toString().toLowerCase();
                            String[] wordsInBook = bookText.split("\\W+");

                            List<String> bookContexts = new ArrayList<>();
                            for (Integer pos : positions) {
                                int start = Math.max(0, pos - contextSize);
                                int end = Math.min(wordsInBook.length, pos + contextSize);
                                String context = String.join(" ", Arrays.asList(wordsInBook).subList(start, end));
                                bookContexts.add(context);
                            }

                            contexts.put(documentName, bookContexts);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the binary index file or document files: " + e.getMessage());
        }

        return contexts;
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
