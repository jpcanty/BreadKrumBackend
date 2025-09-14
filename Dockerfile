# ---- Build stage ----
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy all project files
COPY . .

# Make Gradle wrapper executable
RUN chmod +x ./gradlew

# Build the fat JAR (skip tests for faster deploy)
RUN ./gradlew clean build -x test

# ---- Runtime stage ----
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run Spring Boot, binding to Render's assigned port
CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT"]

