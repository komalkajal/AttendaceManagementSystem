# Attendance Management System

A comprehensive Attendance Management System built with Java, Servlets, JSP, and XML. This system digitizes the attendance process, allowing teachers to mark and update attendance records in real-time, while students can check their attendance status online.

## Features

### User Roles
- **Admin**: Manage classes, users, and reports
- **Teacher**: Mark and update attendance for classes
- **Student**: View attendance percentage and history

### Core Functionality
- **Attendance Recording**: Mark daily attendance for each class
- **Bulk Upload**: Support XML-based bulk upload for entire classes
- **XML Backend Integration**: Attendance data stored and exchanged using XML files
- **Automated Percentage Calculation**: Generate subject-wise and overall attendance percentage
- **Reporting & Analytics**: Generate monthly/semester attendance reports
- **Export Reports**: Export reports in XML/CSV formats
- **Search & Filter**: Filter by class, subject, date, or student
- **Notifications & Alerts**: Highlight students with low attendance

## Technology Stack
- **Backend**: Java Servlets, JSP
- **Database**: MySQL (Production) / H2 (Development)
- **Data Exchange**: XML
- **Frontend**: HTML, CSS, JSP
- **Build Tool**: Maven

## Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL (for production) or H2 (for development)
- Servlet container (Tomcat 9+)

## Installation & Setup

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd attendance-management-system
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Run with embedded Tomcat (if using Spring Boot) or deploy to Tomcat**
   ```bash
   # Deploy the WAR file to your Tomcat server
   cp target/attendance-management-system.war $TOMCAT_HOME/webapps/
   ```

4. **Access the application**
   - Open your browser and navigate to `http://localhost:8080/attendance-management-system`
   - Default admin credentials: `admin` / `admin123`

### Database Configuration

#### For Development (H2)
The application uses H2 in-memory database by default. No additional configuration needed.

#### For Production (MySQL)
1. Create a MySQL database
2. Set environment variables:
   ```bash
   export JDBC_DATABASE_URL=jdbc:mysql://localhost:3306/attendance_db
   export JDBC_DATABASE_USERNAME=your_username
   export JDBC_DATABASE_PASSWORD=your_password
   ```

## Deployment on Render

### Prerequisites
1. A Render account
2. A MySQL database (you can use Render's PostgreSQL or external MySQL)

### Steps

1. **Create a new Web Service on Render**
   - Connect your GitHub repository
   - Select "Java" as the environment
   - Set the build command: `mvn clean package`
   - Set the start command: `java -cp target/attendance-management-system.jar com.attendance.EmbeddedTomcatServer`

2. **Configure Environment Variables**
   - `JDBC_DATABASE_URL`: Your database connection URL (e.g., `jdbc:mysql://host:port/database`)
   - `JDBC_DATABASE_USERNAME`: Your database username
   - `JDBC_DATABASE_PASSWORD`: Your database password
   - `PORT`: Will be automatically set by Render (defaults to 8080)

3. **Deploy**
   - Render will automatically build and deploy your application
   - The application will be available at `https://your-app-name.onrender.com`

### Alternative: Using Docker on Render

If you prefer using Docker:

1. Create a `Dockerfile`:
```dockerfile
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/attendance-management-system.jar .
COPY --from=build /app/src/main/webapp ./webapp
EXPOSE 8080
CMD ["java", "-cp", "attendance-management-system.jar", "com.attendance.EmbeddedTomcatServer"]
```

2. Use Render's Docker deployment option

### Alternative: Using Tomcat on Render

If you prefer to use Tomcat, you can:

1. Use Render's Docker support
2. Create a Dockerfile that includes Tomcat
3. Deploy the WAR file to the Tomcat server

Example Dockerfile:
```dockerfile
FROM tomcat:9-jdk17
COPY target/attendance-management-system.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

## Usage

### Admin
1. Login with admin credentials
2. Manage users (create students, teachers)
3. Manage classes and subjects
4. View reports and analytics

### Teacher
1. Login with teacher credentials
2. Select class, subject, and date
3. Mark attendance for students
4. Upload XML files for bulk attendance
5. View attendance reports

### Student
1. Login with student credentials
2. View attendance history
3. Check attendance percentage
4. Download attendance reports (XML/CSV)

## XML Format for Bulk Upload

```xml
<attendances>
    <attendance>
        <studentId>1</studentId>
        <subjectId>1</subjectId>
        <classId>1</classId>
        <date>2024-01-15</date>
        <status>PRESENT</status>
        <notes>Optional notes</notes>
        <checkInTime>09:00:00</checkInTime>
        <checkOutTime>17:00:00</checkOutTime>
    </attendance>
</attendances>
```

## Project Structure

```
attendance-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/attendance/
│   │   │       ├── dao/          # Data Access Objects
│   │   │       ├── model/        # Entity models
│   │   │       ├── servlet/      # Servlets
│   │   │       └── util/         # Utility classes
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml       # Web configuration
│   │       ├── admin/            # Admin pages
│   │       ├── teacher/          # Teacher pages
│   │       ├── student/          # Student pages
│   │       ├── css/              # Stylesheets
│   │       └── *.jsp             # JSP pages
│   └── test/
├── pom.xml                       # Maven configuration
├── render.yaml                   # Render deployment config
└── README.md                     # This file
```

## Default Credentials

- **Admin**: `admin` / `admin123`
- **Teacher**: Create via admin panel
- **Student**: Create via admin panel

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is open source and available under the MIT License.

## Support

For issues and questions, please open an issue on GitHub.

## Future Enhancements

- Biometric attendance integration
- Email notifications for low attendance
- Mobile app support
- Integration with ERP systems
- Advanced analytics and reporting
- Multi-language support

