# Search Engine Project - Stage 2

## Cover Page:
- **Subject:** Big Data (BD)
- **Academic Year:** 2024-2025
- **Degree:** Data Science and Engineering (GCID)
- **School:** School of Computer Engineering (EII)
- **University:** University of Las Palmas de Gran Canaria (ULPGC)

## Project Overview:
This project implements a comprehensive search engine designed to process and query large volumes of textual data, using principles of Big Data management. The project started with a Python implementation and has now been migrated to Java to enhance robustness and scalability, introducing optimized data structures, parallelization, and benchmarking tools to assess performance.

The system efficiently performs document crawling, indexing, and querying, making it a robust solution for handling large-scale document retrieval and analysis.

## Main Functionality:
- **Document Crawling:** Downloads and stores documents from Project Gutenberg into a datalake for further processing.
- **Data Indexing:** Processes documents and creates an inverted index, as well as a metadata structure that enables fast term retrieval.
- **Query Engine:** Allows users to perform searches on the indexed documents, supporting queries based on words and metadata.
- **UI**: Offers an intuitive interface for querying documents and visualizing results, communicating with the Query Engine via a REST API.
- **Data Visualization:** Scripts provide visual representations of the data, including word frequency and indexing metrics.

## Project Structure:
The project is organized into independent modules, ensuring modular and scalable development:

- **Crawler:** Downloads and stores documents, ensuring availability for indexing.
- **Datalake:** Repository for raw, unprocessed data.
- **Datamart:** Contains processed data, including the inverted index and metadata, for optimized querying.
- **Indexer:** Manages document indexing using multiple data structures for the inverted index to test and evaluate performance and scalability.
- **Query Engine:** Provides core functionality for querying indexed documents. It also manages API interactions and endpoints for document and query handling.
- **UI (User Interface):** A React-based, user-friendly interface for interacting with the search engine.
- **Plot Generator:** Generates visualizations to analyze indexing and querying performance.

## Development Environment:
- **IDE:** IntelliJ IDEA.
- **Version Control:** Git & GitHub for source code management and collaboration.
- **Dependency Management:** Maven for dependency management and module building.
- **Containerization:** Dockerfiles for each module to enable easy deployment and scalability across environments.

### Docker Configuration:
Each module includes a dedicated `Dockerfile`. For example, the Dockerfile for the crawler is configured as follows:

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/crawler-1.0-SNAPSHOT.jar crawler.jar
ENTRYPOINT ["java", "-jar", "crawler.jar"]

````

## Running the Application:

1. Clone the Repository: Clone the project repository.
2. Install Dependencies: Use Maven to install dependencies in the main directory

````bash
mvn clean package -DskipTests
````
3. Build and Start Docker Containers: Navigate to the project’s root directory and run:

````bash
docker compose down
````

````bash
docker compose build
````
````bash
docker compose up
````

4. Running Modules: Each module runs automatically within its Docker container. Use tools like Postman or cURL to interact with the API. In docker all modules are connected. We created volumes for storing book data and processed indexes and metadata. Individual modules have specific access to them. Additionally, when docker is launched, all necessary packages are downloaded and the application starts working.

## SOLID Principles and Design Patterns
The project follows the five SOLID principles for maintainability and scalability, using design patterns such as the Factory Method to ensure the system is extensible and robust.

- **Single Responsibility Principle (SRP):** Each class and module in the project has a single, well-defined responsibility. For instance, the `Crawler` module is solely responsible for downloading and storing documents, while the `Indexer` is dedicated to indexing them, making the code easier to maintain and test.

- **Open/Closed Principle (OCP):** The project’s design allows modules to be extended without modifying existing code. For example, new indexing strategies can be implemented and integrated into the system without altering the core indexing logic.

- **Liskov Substitution Principle (LSP):** This principle ensures that subclasses can replace their base classes without altering the system's functionality. In this project, interfaces are used for key components like indexing and querying. Any new implementation (e.g., a different database or data structure) can replace the existing one without impacting other parts of the system.

- **Interface Segregation Principle (ISP):** Interfaces are designed to be specific, ensuring that classes only depend on methods they actually use. This results in more modular and focused code, as seen in the separation of interfaces for different functionalities within the API and data access layers.

- **Dependency Inversion Principle (DIP):** High-level modules do not depend on low-level modules; both depend on abstractions. For example, the `Query Engine` interacts with the `Indexer` through interfaces, making it possible to change the underlying data structure or indexing mechanism without modifying the query logic.

## Testing:
- **Benchmarking with JMH**: Benchmarks are conducted with JMH, comparing the Java implementation with the original Python version in terms of execution time, memory consumption, and scalability.

## Additional Highlights:

- **Modular Architecture**: The project’s modular structure makes it easy to extend, improve, and debug.
- **Scalability and Optimization**: Designed to handle large data volumes efficiently, with multiple data structures tested and applied parallelization where possible.
- **Cross-Language Comparison**: Performance improvements from the Python to Java migration are documented, highlighting gains in efficiency and scalability.
- **Visualization**: Graph scripts provide insights into word frequency and indexing patterns, helping users analyze data trends and performance metrics.

## Participants:

- María Alonso León
- Víctor Gil Bernal
- Jacob Jażdżyk
- Kimberly Casimiro Torres
