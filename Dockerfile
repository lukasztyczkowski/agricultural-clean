# Krok 1: Budowanie aplikacji Spring Boot + Vaadin w trybie produkcyjnym
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Profil -Pproduction instruuje Vaadina, aby zbudował i zoptymalizował frontend
RUN mvn clean package -Pproduction -DskipTests

# Krok 2: Uruchomienie zoptymalizowanego pliku .jar
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
