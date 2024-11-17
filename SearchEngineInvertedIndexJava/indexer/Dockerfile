FROM openjdk:22-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y wget \
    && wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
    && chmod +x wait-for-it.sh

COPY target/indexer-1.0-SNAPSHOT-fat.jar indexer.jar

ENTRYPOINT ["./wait-for-it.sh", "mongodb:27017", "--", "java", "-jar", "indexer.jar"]
