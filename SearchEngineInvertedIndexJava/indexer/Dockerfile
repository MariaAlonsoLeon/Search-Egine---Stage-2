FROM openjdk:21-jdk
COPY target/indexer-1.0-SNAPSHOT-fat.jar /app/indexer.jar
WORKDIR /app
CMD ["java", "-jar", "indexer.jar"]
