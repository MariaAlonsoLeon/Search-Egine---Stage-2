# Użyj obrazu JDK jako podstawowego
FROM openjdk:21-jdk-slim

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj plik JAR do kontenera
COPY target/indexer-1.0-SNAPSHOT.jar indexer.jar

# Uruchom aplikację
CMD ["java", "-jar", "indexer.jar"]
