# Stage 1: Build Stage
FROM gradle:8.3-jdk20 AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle build --no-daemon

# Stage 2: Runtime Stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Exponiere den Port 8080, den Spring Boot verwendet
EXPOSE 8080

# Führe die Spring Boot-Anwendung aus
ENTRYPOINT ["java", "-jar", "app.jar"]
