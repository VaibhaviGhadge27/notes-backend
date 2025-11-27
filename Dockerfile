# Use Eclipse Temurin (Java) image
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw -q -DskipTests package

# ------------------
# Run stage
# ------------------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
