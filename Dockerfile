FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .

# 🔥 KLUCZ: pełny Vaadin build (z frontendem)
RUN mvn clean package \
    -Pproduction \
    -Dvaadin.productionMode=true \
    -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]




