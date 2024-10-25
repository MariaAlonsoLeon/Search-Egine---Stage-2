# Użyj obrazu JDK jako podstawowego
FROM openjdk:21-jdk-slim

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj plik JAR do kontenera
COPY target/queryengine-1.0-SNAPSHOT.jar queryengine.jar

# Uruchom aplikację
CMD ["java", "-jar", "queryengine.jar"]
