package org.example;

import static spark.Spark.*;
import org.example.controller.QueryController;

public class QueryEngineApplication {
    public static void main(String[] args) {
        port(8080);

        QueryController controller = new QueryController();
        controller.registerRoutes();

        System.out.println("Query Engine API is running on http://localhost:8080");
    }
}