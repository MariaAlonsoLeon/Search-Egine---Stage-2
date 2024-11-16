package org.example.factory;

import org.example.DataType;
import org.example.helper.*;

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
