package org.example;

import static spark.Spark.*;
import org.example.controller.QueryController;

public class QueryEngineApplication {
    public static void main(String[] args) {
        port(8080);

        after((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        QueryController controller = new QueryController();
        controller.registerRoutes();

        System.out.println("Query Engine API is running on http://localhost:8080");
    }
}
