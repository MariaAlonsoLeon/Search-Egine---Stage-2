FROM openjdk:22-jdk-slim
WORKDIR /app
COPY target/indexer-1.0-SNAPSHOT-fat.jar indexer.jar
CMD ["java", "-jar", "indexer.jar"]
