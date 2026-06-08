# Krok 1: Budowanie aplikacji Spring Boot + Vaadin przy użyciu Javy 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Budujemy aplikację bez flagi -Pproduction, za to z flagą Vaadin mode w argumentach
RUN mvn clean package -Dvaadin.productionMode=true -DskipTests

# Krok 2: Uruchomienie zoptymalizowanego pliku .jar na Javie 21
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
