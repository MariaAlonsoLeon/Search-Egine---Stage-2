FROM openjdk:22-jdk-slim
WORKDIR /app
COPY target/crawler-1.0-SNAPSHOT.jar crawler.jar
ENTRYPOINT ["java", "-jar", "crawler.jar"]
