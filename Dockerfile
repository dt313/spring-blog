# Stage 1: Build the JAR
FROM maven:3.9-eclipse-temurin AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM openjdk:22-jdk-slim
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]