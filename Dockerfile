# Use ARM64-compatible base image for Java 23
FROM eclipse-temurin:23-jdk as builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew build.gradle settings.gradle ./ 
COPY gradle ./gradle

# Make Gradle wrapper executable
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the source code
COPY src ./src

# Build the application
RUN ./gradlew clean build -x test --no-daemon

# Use JRE for the final image
FROM eclipse-temurin:23-jre

# Set working directory
WORKDIR /app

# Copy the built jar from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
