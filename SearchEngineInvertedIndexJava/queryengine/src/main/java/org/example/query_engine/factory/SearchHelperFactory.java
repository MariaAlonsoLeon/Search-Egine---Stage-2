package org.example.query_engine.factory;

import org.example.query_engine.DataType;
import org.example.query_engine.helper.*;

public class SearchHelperFactory {
    public static SearchHelper getHelper(DataType dataType) {
        switch (dataType) {
            case JSON:
                return new JsonSearchHelper();
            case BINARY:
                return new BinarySearchHelper();
            case NEO4J:
                return new Neo4jSearchHelper();
            case MONGODB:
                return new MongoDbSearchHelper();
            default:
                throw new IllegalArgumentException("Invalid data type");
        }
    }
}
