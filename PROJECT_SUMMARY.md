# Attendance Management System - Project Summary

## Overview
A comprehensive Attendance Management System built with Java, Servlets, JSP, and XML. This system digitizes the attendance process for educational institutions, supporting multiple user roles with dedicated functionalities.

## Key Features Implemented

### ✅ User Roles
- **Admin**: Full system management (users, classes, subjects, reports)
- **Teacher**: Mark attendance, upload XML files, view reports
- **Student**: View attendance history, check percentage, download reports

### ✅ Attendance Recording
- Mark daily attendance for each class and subject
- Support for multiple statuses (Present, Absent, Late, Excused)
- Check-in and check-out time tracking
- Notes field for additional information

### ✅ XML Backend Integration
- Bulk upload attendance records via XML files
- Export attendance reports in XML format
- XML-based data exchange for platform independence

### ✅ Automated Percentage Calculation
- Subject-wise attendance percentage
- Overall attendance percentage
- Real-time calculation and display

### ✅ Reporting & Analytics
- Generate attendance reports by student
- Filter by date range, subject, class
- Export reports in XML and CSV formats
- Monthly/semester report generation capability

### ✅ Search & Filter Options
- Filter attendance by class, subject, date
- Search student attendance history
- Quick access to attendance records

### ✅ Additional Features
- User authentication and session management
- Responsive web interface
- Database initialization on first run
- Support for both H2 (development) and MySQL/PostgreSQL (production)

## Technology Stack

- **Backend**: Java Servlets, JSP
- **Database**: MySQL/PostgreSQL (Production), H2 (Development)
- **Data Exchange**: XML (JAXB, Jackson)
- **Frontend**: HTML, CSS, JSP, JSTL
- **Build Tool**: Maven
- **Server**: Embedded Tomcat (for deployment)

## Project Structure

```
attendance-management-system/
├── src/main/java/com/attendance/
│   ├── dao/                    # Data Access Objects
│   │   ├── AttendanceDAO.java
│   │   ├── ClassDAO.java
│   │   ├── SubjectDAO.java
│   │   └── UserDAO.java
│   ├── model/                  # Entity Models
│   │   ├── Attendance.java
│   │   ├── Class.java
│   │   ├── Subject.java
│   │   └── User.java
│   ├── servlet/                # Servlets
│   │   ├── AdminServlet.java
│   │   ├── AttendanceServlet.java
│   │   ├── LoginServlet.java
│   │   ├── LogoutServlet.java
│   │   ├── ReportServlet.java
│   │   └── XMLUploadServlet.java
│   ├── util/                   # Utility Classes
│   │   ├── DatabaseUtil.java
│   │   └── XMLHandler.java
│   └── EmbeddedTomcatServer.java  # Embedded server for deployment
├── src/main/webapp/
│   ├── WEB-INF/
│   │   └── web.xml            # Web configuration
│   ├── admin/                 # Admin pages
│   │   ├── dashboard.jsp
│   │   ├── users.jsp
│   │   ├── classes.jsp
│   │   └── subjects.jsp
│   ├── teacher/               # Teacher pages
│   │   ├── dashboard.jsp
│   │   ├── mark-attendance.jsp
│   │   └── upload-xml.jsp
│   ├── student/               # Student pages
│   │   ├── dashboard.jsp
│   │   └── view-attendance.jsp
│   ├── css/
│   │   └── style.css
│   ├── index.jsp
│   ├── login.jsp
│   └── error.jsp
├── pom.xml                    # Maven configuration
├── render.yaml                # Render deployment config
├── README.md                  # Main documentation
├── DEPLOYMENT.md              # Deployment guide
└── .gitignore
```

## Database Schema

### Users Table
- id, username, password, email, full_name, role, class_id, student_id

### Classes Table
- id, class_name, section, teacher_id, academic_year

### Subjects Table
- id, subject_name, subject_code, class_id, teacher_id

### Attendance Table
- id, student_id, subject_id, class_id, date, status, notes, check_in_time, check_out_time
- Unique constraint on (student_id, subject_id, date)

## Default Credentials

- **Admin**: `admin` / `admin123`

## API Endpoints

### Authentication
- `GET/POST /login` - Login page and authentication
- `GET /logout` - Logout

### Attendance
- `GET /attendance/mark` - Mark attendance page
- `POST /attendance/mark` - Submit attendance
- `GET /attendance/view` - View attendance (student)
- `GET /attendance/student/{id}` - Get student attendance

### Admin
- `GET /admin/users` - Manage users
- `POST /admin/users/create` - Create user
- `GET /admin/classes` - Manage classes
- `POST /admin/classes/create` - Create class
- `GET /admin/subjects` - Manage subjects
- `POST /admin/subjects/create` - Create subject

### Reports
- `GET /reports/?format=xml&studentId={id}` - Export XML report
- `GET /reports/?format=csv&studentId={id}` - Export CSV report

### XML Upload
- `POST /upload-xml` - Upload XML file for bulk attendance

## Deployment

### Local Development
1. Build: `mvn clean package`
2. Run: `java -jar target/attendance-management-system.jar`
3. Access: http://localhost:8080

### Render Deployment
1. Push code to GitHub
2. Create Web Service on Render
3. Set build command: `mvn clean package && mkdir -p target/webapp && cp -r src/main/webapp/* target/webapp/`
4. Set start command: `java -jar target/attendance-management-system.jar`
5. Configure environment variables (database credentials)
6. Deploy!

See DEPLOYMENT.md for detailed instructions.

## Future Enhancements

- [ ] Email notifications for low attendance
- [ ] Biometric attendance integration
- [ ] Mobile app support
- [ ] Advanced analytics dashboard
- [ ] Integration with ERP systems
- [ ] Multi-language support
- [ ] Real-time attendance tracking
- [ ] QR code-based attendance
- [ ] Attendance trend analysis
- [ ] Automated report scheduling

## License

This project is open source and available for educational purposes.

## Support

For issues or questions, please refer to the documentation or create an issue in the repository.

