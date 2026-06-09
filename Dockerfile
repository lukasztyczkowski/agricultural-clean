# Krok 1: Budowanie i pełna produkcyjna kompilacja frontendu
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Używamy jawnej flagi wymuszenia kompilacji frontendu zamiast nieistniejącego profilu
RUN mvn clean package -Dvaadin.force.production.build=true -DskipTests

# Krok 2: Uruchomienie aplikacji z pełnymi zasobami i motywem
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]





