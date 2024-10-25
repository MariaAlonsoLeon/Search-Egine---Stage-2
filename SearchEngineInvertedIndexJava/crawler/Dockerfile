# Użyj obrazu JDK jako podstawowego
FROM openjdk:21-jdk-slim

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj plik JAR do kontenera
COPY target/crawler-1.0-SNAPSHOT.jar crawler.jar

# Uruchom aplikację
CMD ["java", "-jar", "crawler.jar"]
