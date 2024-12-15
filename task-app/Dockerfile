# Stage 1: Build the application
FROM maven:3.8.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/task-api-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
