package org.example.query_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueryEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueryEngineApplication.class, args);
        System.out.println("Query Engine API is running on http://localhost:8080");
    }
}