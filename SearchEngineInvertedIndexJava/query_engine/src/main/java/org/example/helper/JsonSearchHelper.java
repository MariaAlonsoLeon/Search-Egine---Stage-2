package org.example.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonSearchHelper implements SearchHelper {

    private static final String INVERTED_INDEX_REPOSITORY = "datalake_inverted_index";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String searchWord(String word) {
        return searchWords(Collections.singletonList(word));
    }

    private String searchWords(List<String> words) {
        Map<String, Map<String, Object>> results = new HashMap<>();
        searchInvertedIndex(words, true, results);
        return convertResultsToJson(results);
    }

    @Override
    public String searchAnd(String word1, String word2) {
        Map<String, Map<String, Object>> results = new HashMap<>();
        List<String> words = Arrays.asList(word1, word2);
        searchInvertedIndex(words, true, results);
        return convertResultsToJson(results);
    }

    @Override
    public String searchOr(String word1, String word2) {
        Map<String, Map<String, Object>> results = new HashMap<>();
        List<String> words = Arrays.asList(word1, word2);
        searchInvertedIndex(words, false, results);
        return convertResultsToJson(results);
    }

    @Override
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
                        results.put(jsonFile.getName(), getFileData(rootNode, Collections.emptyList()));
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file: " + jsonFile.getName());
                    e.printStackTrace();
                }
            }
        }

        return convertResultsToJson(results);
    }

    @Override
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

        return convertResultsToJson(paginatedResult);
    }

    private void searchInvertedIndex(List<String> words, boolean requireAll, Map<String, Map<String, Object>> results) {
        File indexRepository = new File(INVERTED_INDEX_REPOSITORY);
        if (!indexRepository.exists() || !indexRepository.isDirectory()) {
            return;
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
                            if (requireAll && containsAll) {
                                containsAll = rootNode.get(word).get("frequency").asInt() > 0;
                            }
                        } else {
                            containsAll = false;
                        }
                    }

                    if (requireAll && containsAll || !requireAll && containsAny) {
                        results.put(jsonFile.getName(), getFileData(rootNode, words));
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file: " + jsonFile.getName());
                    e.printStackTrace();
                }
            }
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

    private String convertResultsToJson(Object results) {
        try {
            return objectMapper.writeValueAsString(results);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
