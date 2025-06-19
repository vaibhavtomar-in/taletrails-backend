# Use OpenJDK 21 as the base image
FROM openjdk:21-slim

# Set working directory
WORKDIR /app

# Copy the Gradle files first to leverage Docker cache
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Make the gradlew script executable
RUN chmod +x gradlew

# Copy the source code
COPY src ./src

# Build the application
RUN ./gradlew build -x test

# Find the jar file and move it to a known location
RUN mv $(find build/libs/ -name '*.jar' ! -name '*plain.jar') app.jar

# Expose the port the app runs on
EXPOSE 8080


# CMD ["java", "-jar", "/app/build/libs/*.jar"]
# Run the Spring Boot application directly from the source
#CMD ["./gradlew", "bootRun"]

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]