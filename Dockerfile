# Krok 1: Budowanie aplikacji Spring Boot + Vaadin w trybie produkcyjnym
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Wymuszamy pełny profil produkcyjny Vaadina podczas kompilacji Mavena
RUN mvn clean package -Pproduction -DskipTests

# Krok 2: Uruchomienie gotowej, ostylowanej aplikacji
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

