# ETAP 1: Przygotowanie i produkcyjne zbudowanie frontendu (Vaadin potrzebuje Node)
FROM node:20 AS frontend-stage
WORKDIR /app
COPY . .
# Vaadin potrzebuje pobrać i zainstalować zależności frontu przed kompilacją pliku jar
RUN npm install --production --no-audit --no-fund

# ETAP 2: Budowanie pliku .jar w środowisku Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build-stage
WORKDIR /app
COPY . .
# Kopiujemy przygotowane node_modules z poprzedniego etapu
COPY --from=frontend-stage /app/node_modules ./node_modules
RUN mvn clean package -Pproduction -Dvaadin.productionMode=true -DskipTests

# ETAP 3: Uruchomienie gotowej, w pełni ostylowanej aplikacji
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build-stage /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]


