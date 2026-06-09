# Krok 1: Budowanie i pełna produkcyjna kompilacja frontendu (Vite)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Ta komenda w Vaadin 24 automatycznie pobierze Node.js i zbuduje produkcyjne style do wnętrza pliku .jar
RUN mvn clean package -Pproduction -DskipTests

# Krok 2: Uruchomienie aplikacji z pełnymi zasobami
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]




