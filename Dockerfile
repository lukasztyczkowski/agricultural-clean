# Krok 1: Budowanie i kompilacja frontendu z Node.js i Mavenem
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Instalacja Node.js wewnątrz kontenera (wymagane przez Vaadin do stylów)
RUN curl -sL https://nodesource.com | bash - && \
    apt-get install -y nodejs

COPY . .
# Budowanie aplikacji z jawnym wymuszeniem trybu produkcyjnego i kompilacji frontendu
RUN mvn clean package -Pproduction -Dvaadin.productionMode=true -DskipTests

# Krok 2: Uruchomienie gotowej i w pełni ostylowanej aplikacji
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

