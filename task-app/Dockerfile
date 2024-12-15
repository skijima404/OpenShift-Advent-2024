# Stage 1: Build the application using Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -X

# Stage 2: Create the final lightweight image
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/task-api.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
