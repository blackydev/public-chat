# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app
COPY your-application.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
