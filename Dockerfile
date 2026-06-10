FROM maven:3.9.6-eclipse-temurin-21 AS build

RUN apt-get update && apt-get install -y curl \
    && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y nodejs

WORKDIR /app
COPY . .

RUN mvn clean package -Pproduction -DskipTests




