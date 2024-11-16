FROM openjdk:22-jdk-slim
WORKDIR /app
COPY target/query_engine-1.0-SNAPSHOT.jar query_engine.jar
ENTRYPOINT ["java", "-jar", "query_engine.jar"]
