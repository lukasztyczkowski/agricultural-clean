# ETAP 1: Przygotowanie i produkcyjne zbudowanie frontendu
FROM node:20 AS frontend-stage
WORKDIR /app
COPY . .
RUN npm install --production --no-audit --no-fund

# ETAP 2: Budowanie pliku .jar w środowisku Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build-stage
WORKDIR /app
COPY . .
COPY --from=frontend-stage /app/node_modules ./node_modules
# Zamiast profilu -Pproduction, wywołujemy wtyczkę Vaadina bezpośrednio
RUN mvn clean vaadin:build-frontend package -Dvaadin.productionMode=true -DskipTests

# ETAP 3: Uruchomienie gotowej, w pełni ostylowanej aplikacji
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build-stage /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]



