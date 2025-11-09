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

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy built JAR and webapp from build stage
COPY --from=build /app/target/attendance-management-system.jar .
COPY --from=build /app/target/webapp ./webapp
COPY --from=build /app/target/classes ./classes

# Expose port (Render will set PORT env variable)
EXPOSE 8080

# Health check (optional - Render handles this)
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-8080}/index.jsp || exit 1

# Run the application
CMD ["java", "-jar", "attendance-management-system.jar"]

