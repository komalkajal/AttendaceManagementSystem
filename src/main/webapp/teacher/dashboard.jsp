<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object userObj = session.getAttribute("user");
    if (userObj == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    com.attendance.model.User user = (com.attendance.model.User) userObj;
    if (!"TEACHER".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Teacher Dashboard - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>Teacher Dashboard</h1>
            <div class="nav-menu">
                <a href="dashboard.jsp">Dashboard</a>
                <a href="../attendance/mark">Mark Attendance</a>
                <a href="../upload-xml">Upload XML</a>
                <a href="../logout">Logout</a>
            </div>
        </div>

        <% if (success != null) { %>
            <div class="success-message"><%= success %></div>
        <% } %>
        <% if (error != null) { %>
            <div class="error-message"><%= error %></div>
        <% } %>

        <div class="stats-grid">
            <div class="stat-card">
                <p>Welcome, <%= user.getFullName() %></p>
                <h3>Teacher</h3>
            </div>
        </div>

        <div class="grid">
            <div class="card">
                <h3>Quick Actions</h3>
                <ul style="list-style: none; padding: 0;">
                    <li style="margin-bottom: 10px;">
                        <a href="../attendance/mark" class="btn btn-primary" style="width: auto;">Mark Attendance</a>
                    </li>
                    <li style="margin-bottom: 10px;">
                        <a href="../upload-xml" class="btn btn-success" style="width: auto;">Upload XML File</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>

