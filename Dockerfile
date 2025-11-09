# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Create webapp directory
RUN mkdir -p target/webapp && cp -r src/main/webapp/* target/webapp/

# Debug: List all files in target
RUN echo "=== Checking target directory ===" && \
    ls -la target/ && \
    echo "=== Looking for JAR files ===" && \
    find target -name "*.jar" -type f || echo "No JAR files found"

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy webapp and classes
COPY --from=build /app/target/webapp ./webapp
COPY --from=build /app/target/classes ./classes

# Copy the JAR file - shade plugin should create it with finalName
# But we'll use a wildcard to catch any JAR file name
COPY --from=build /app/target/*.jar ./app.jar

# Verify files are copied
RUN ls -la && \
    ls -la webapp/ && \
    ls -la *.jar || (echo "ERROR: No JAR file found!" && exit 1)

# Expose port (Render will set PORT env variable)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
