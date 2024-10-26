FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/queryengine-1.0-SNAPSHOT.jar queryengine.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "queryengine.jar"]
