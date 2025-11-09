<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.attendance.dao.AttendanceDAO" %>
<%@ page import="com.attendance.model.*" %>
<%@ page import="java.util.*" %>
<%
    Object userObj = session.getAttribute("user");
    if (userObj == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    User user = (User) userObj;
    if (!"STUDENT".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
    
    AttendanceDAO attendanceDAO = new AttendanceDAO();
    double overallPercentage = attendanceDAO.getOverallAttendancePercentage(user.getId());
%>
<!DOCTYPE html>
<html>
<head>
    <title>Student Dashboard - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>Student Dashboard</h1>
            <div class="nav-menu">
                <a href="dashboard.jsp">Dashboard</a>
                <a href="../attendance/view">View Attendance</a>
                <a href="../logout">Logout</a>
            </div>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <p>Welcome, <%= user.getFullName() %></p>
                <h3><%= String.format("%.1f", overallPercentage) %>%</h3>
                <p>Overall Attendance</p>
            </div>
        </div>

        <div class="grid">
            <div class="card">
                <h3>Quick Actions</h3>
                <ul style="list-style: none; padding: 0;">
                    <li style="margin-bottom: 10px;">
                        <a href="../attendance/view" class="btn btn-primary" style="width: auto;">View My Attendance</a>
                    </li>
                    <li style="margin-bottom: 10px;">
                        <a href="../reports/?format=xml&studentId=<%= user.getId() %>" class="btn btn-success" style="width: auto;">Download XML Report</a>
                    </li>
                    <li style="margin-bottom: 10px;">
                        <a href="../reports/?format=csv&studentId=<%= user.getId() %>" class="btn btn-success" style="width: auto;">Download CSV Report</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>

