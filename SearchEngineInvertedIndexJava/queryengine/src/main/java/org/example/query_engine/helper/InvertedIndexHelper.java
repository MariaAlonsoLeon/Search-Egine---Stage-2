package org.example.query_engine.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class InvertedIndexHelper {

    private static final String INVERTED_INDEX_REPOSITORY = "datalake_inverted_index";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String searchWord(String word) {
        return searchWords(Collections.singletonList(word));
    }

    public String searchWords(List<String> words) {
        Map<String, Map<String, Object>> results = new HashMap<>();

        File indexRepository = new File(INVERTED_INDEX_REPOSITORY);
        if (!indexRepository.exists() || !indexRepository.isDirectory()) {
            return "{\"error\": \"Inverted index repository not found.\"}";
        }

        for (File dateFolder : Objects.requireNonNull(indexRepository.listFiles(File::isDirectory))) {
            for (File jsonFile : Objects.requireNonNull(dateFolder.listFiles((dir, name) -> name.endsWith(".json")))) {
                try {
                    JsonNode rootNode = objectMapper.readTree(jsonFile);
                    boolean containsAll = true;
                    boolean containsAny = false;

                    for (String word : words) {
                        if (rootNode.has(word)) {
                            containsAny = true;
                            if (containsAll) {
                                containsAll = rootNode.get(word).get("frequency").asInt() > 0;
                            }
                        } else {
                            containsAll = false;
                        }
                    }

                    if (containsAll) {
                        results.put(jsonFile.getName(), getFileData(rootNode, words));
                    } else if (containsAny) {
                        results.put(jsonFile.getName(), getFileData(rootNode, words));
                    }

                } catch (IOException e) {
                    System.out.println("Error reading file: " + jsonFile.getName());
                    e.printStackTrace();
                }
            }
        }

        try {
            return objectMapper.writeValueAsString(results);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public String searchAnd(String word1, String word2) {
        Map<String, Map<String, Object>> results = new HashMap<>();
        Map<String, Map<String, Object>> results1 = new HashMap<>();
        Map<String, Map<String, Object>> results2 = new HashMap<>();

        searchInvertedIndex(word1, results1);
        searchInvertedIndex(word2, results2);

        for (String fileName : results1.keySet()) {
            if (results2.containsKey(fileName)) {
                results.put(fileName, results1.get(fileName));
            }
        }

        return convertResultsToJson(results);
    }

    public String searchOr(String word1, String word2) {
        Map<String, Map<String, Object>> results = new HashMap<>();

        searchInvertedIndex(word1, results);
        Map<String, Map<String, Object>> results2 = new HashMap<>();
        searchInvertedIndex(word2, results2);

        results.putAll(results2);

        return convertResultsToJson(results);
    }

    private void searchInvertedIndex(String word, Map<String, Map<String, Object>> results) {
        File indexRepository = new File(INVERTED_INDEX_REPOSITORY);
        if (!indexRepository.exists() || !indexRepository.isDirectory()) {
            return;
        }

        for (File dateFolder : Objects.requireNonNull(indexRepository.listFiles(File::isDirectory))) {
            for (File jsonFile : Objects.requireNonNull(dateFolder.listFiles((dir, name) -> name.endsWith(".json")))) {
                try {
                    JsonNode rootNode = objectMapper.readTree(jsonFile);
                    if (rootNode.has(word)) {
                        JsonNode wordData = rootNode.get(word);
                        Map<String, Object> fileData = new HashMap<>();
                        fileData.put("frequency", wordData.get("frequency").asInt());
                        fileData.put("positions", objectMapper.convertValue(wordData.get("positions"), List.class));
                        results.put(jsonFile.getName(), fileData);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file: " + jsonFile.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private String convertResultsToJson(Map<String, Map<String, Object>> results) {
        try {
            return objectMapper.writeValueAsString(results);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public String searchNot(String word) {
        Map<String, Map<String, Object>> results = new HashMap<>();

        File indexRepository = new File(INVERTED_INDEX_REPOSITORY);
        if (!indexRepository.exists() || !indexRepository.isDirectory()) {
            return "{\"error\": \"Inverted index repository not found.\"}";
        }

        for (File dateFolder : Objects.requireNonNull(indexRepository.listFiles(File::isDirectory))) {
            for (File jsonFile : Objects.requireNonNull(dateFolder.listFiles((dir, name) -> name.endsWith(".json")))) {
                try {
                    JsonNode rootNode = objectMapper.readTree(jsonFile);
                    if (!rootNode.has(word)) {
                        results.put(jsonFile.getName(), getFileData(rootNode, Collections.singletonList(word)));
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file: " + jsonFile.getName());
                    e.printStackTrace();
                }
            }
        }

        try {
            return objectMapper.writeValueAsString(results);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public String paginate(String word, int pageSize, int pageNumber) {
        List<Map<String, Object>> wordList = new ArrayList<>();
        File indexRepository = new File(INVERTED_INDEX_REPOSITORY);
        if (!indexRepository.exists() || !indexRepository.isDirectory()) {
            return "{\"error\": \"Inverted index repository not found.\"}";
        }

        for (File dateFolder : Objects.requireNonNull(indexRepository.listFiles(File::isDirectory))) {
            for (File jsonFile : Objects.requireNonNull(dateFolder.listFiles((dir, name) -> name.endsWith(".json")))) {
                try {
                    JsonNode rootNode = objectMapper.readTree(jsonFile);
                    if (rootNode.has(word)) {
                        Map<String, Object> fileData = getFileData(rootNode, Collections.singletonList(word));
                        wordList.add(fileData);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file: " + jsonFile.getName());
                    e.printStackTrace();
                }
            }
        }

        int totalItems = wordList.size();
        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex >= totalItems || fromIndex < 0) {
            return "{\"error\": \"Invalid page number.\"}";
        }

        List<Map<String, Object>> paginatedList = wordList.subList(fromIndex, toIndex);
        Map<String, Object> paginatedResult = new HashMap<>();
        paginatedResult.put("page", pageNumber);
        paginatedResult.put("size", pageSize);
        paginatedResult.put("total", totalItems);
        paginatedResult.put("results", paginatedList);

        try {
            return objectMapper.writeValueAsString(paginatedResult);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private Map<String, Object> getFileData(JsonNode rootNode, List<String> words) {
        Map<String, Object> fileData = new HashMap<>();
        for (String word : words) {
            if (rootNode.has(word)) {
                JsonNode wordData = rootNode.get(word);
                fileData.put(word, Map.of(
                        "frequency", wordData.get("frequency").asInt(),
                        "positions", objectMapper.convertValue(wordData.get("positions"), List.class)
                ));
            }
        }
        return fileData;
    }
}
