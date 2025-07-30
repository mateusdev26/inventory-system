FROM eclipse-temurin:21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY target/inventory-system-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
