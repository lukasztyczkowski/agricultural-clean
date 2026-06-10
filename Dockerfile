# =========================
# 1. BUILD STAGE (Maven + Node dla Vaadin/Vite)
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Node.js (WYMAGANE dla Vaadin 25 / Vite)
RUN apt-get update && apt-get install -y curl \
    && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y nodejs \
    && node -v && npm -v

WORKDIR /app

COPY . .

# FULL VAADIN PRODUCTION BUILD
RUN mvn clean package -Pproduction -DskipTests


# =========================
# 2. RUNTIME STAGE (light JRE)
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render wymaga portu z ENV
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]




