<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object userObj = session.getAttribute("user");
    if (userObj == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    com.attendance.model.User user = (com.attendance.model.User) userObj;
    if (!"TEACHER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Upload XML - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>Upload XML File</h1>
            <div class="nav-menu">
                <a href="dashboard.jsp">Dashboard</a>
                <a href="../attendance/mark">Mark Attendance</a>
                <a href="../teacher/upload-xml.jsp">Upload XML</a>
                <a href="../logout">Logout</a>
            </div>
        </div>

        <% if (success != null) { %>
            <div class="success-message"><%= success %></div>
        <% } %>
        <% if (error != null) { %>
            <div class="error-message"><%= error %></div>
        <% } %>

        <div class="card">
            <h3>Bulk Upload Attendance from XML</h3>
            <p>Upload an XML file containing attendance records. The XML should have the following structure:</p>
            <pre style="background: #f5f5f5; padding: 15px; border-radius: 5px; overflow-x: auto;">
&lt;attendances&gt;
    &lt;attendance&gt;
        &lt;studentId&gt;1&lt;/studentId&gt;
        &lt;subjectId&gt;1&lt;/subjectId&gt;
        &lt;classId&gt;1&lt;/classId&gt;
        &lt;date&gt;2024-01-15&lt;/date&gt;
        &lt;status&gt;PRESENT&lt;/status&gt;
        &lt;notes&gt;Optional notes&lt;/notes&gt;
        &lt;checkInTime&gt;09:00:00&lt;/checkInTime&gt;
        &lt;checkOutTime&gt;17:00:00&lt;/checkOutTime&gt;
    &lt;/attendance&gt;
&lt;/attendances&gt;
            </pre>
            
            <form action="../upload-xml" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="xmlFile">Select XML File:</label>
                    <input type="file" id="xmlFile" name="xmlFile" accept=".xml" required>
                </div>
                <button type="submit" class="btn btn-primary">Upload and Import</button>
            </form>
        </div>
    </div>
</body>
</html>

