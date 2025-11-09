#!/bin/bash
# Build script for Render deployment

echo "Building Attendance Management System..."

# Clean and package
mvn clean package

# Copy webapp resources to target directory for embedded server
echo "Preparing webapp resources..."
mkdir -p target/webapp
cp -r src/main/webapp/* target/webapp/

echo "Build complete!"
echo "To run: java -cp target/attendance-management-system.jar:target/classes com.attendance.EmbeddedTomcatServer"

