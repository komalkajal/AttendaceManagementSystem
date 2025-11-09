<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Object userObj = session.getAttribute("user");
    if (userObj == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    com.attendance.model.User user = (com.attendance.model.User) userObj;
    if (!"ADMIN".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>Admin Dashboard</h1>
            <div class="nav-menu">
                <a href="dashboard.jsp">Dashboard</a>
                <a href="../admin/users">Users</a>
                <a href="classes.jsp">Classes</a>
                <a href="subjects.jsp">Subjects</a>
                <a href="../logout">Logout</a>
            </div>
        </div>

        <% if (success != null) { %>
            <div class="success-message"><%= success %></div>
        <% } %>

        <div class="stats-grid">
            <div class="stat-card">
                <p>Welcome, <%= user.getFullName() %></p>
                <h3>Administrator</h3>
            </div>
        </div>

        <div class="grid">
            <div class="card">
                <h3>Quick Actions</h3>
                <ul style="list-style: none; padding: 0;">
                    <li style="margin-bottom: 10px;"><a href="../admin/users?role=STUDENT" class="btn btn-primary" style="width: auto;">Manage Students</a></li>
                    <li style="margin-bottom: 10px;"><a href="../admin/users?role=TEACHER" class="btn btn-primary" style="width: auto;">Manage Teachers</a></li>
                    <li style="margin-bottom: 10px;"><a href="classes.jsp" class="btn btn-primary" style="width: auto;">Manage Classes</a></li>
                    <li style="margin-bottom: 10px;"><a href="subjects.jsp" class="btn btn-primary" style="width: auto;">Manage Subjects</a></li>
                </ul>
            </div>

            <div class="card">
                <h3>System Information</h3>
                <p>Attendance Management System v1.0</p>
                <p>Manage users, classes, subjects, and view reports.</p>
            </div>
        </div>
    </div>
</body>
</html>

