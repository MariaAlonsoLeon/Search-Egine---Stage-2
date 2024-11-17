package Benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BookReader {
    private static String FILENAME = "datalake/20241116/book_";

    public static Map<String, String> read_books(int N){
        Map<String, String> result = new HashMap<>();

        for (int i = 1; i <= N; i++) {
            String fileName = FILENAME + i + ".txt";
            StringBuilder content = new StringBuilder();

            File file = new File(fileName);
            if (!file.exists() || !file.canRead()) {
                System.out.println("File does not exist or cannot be read: " + fileName);
                result.put("error_" + i, "File does not exist or cannot be read: " + fileName);
                continue;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }

                result.put("book_" + i, content.toString());
            } catch (IOException e) {
                e.printStackTrace();
                result.put("error_" + i, "Error reading file: " + e.getMessage());
            }
        }

        return result;
    }
}