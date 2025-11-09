<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object user = session.getAttribute("user");
    if (user != null) {
        response.sendRedirect("dashboard");
        return;
    }
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Attendance Management System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="login-container">
        <div class="login-box">
            <h2>Login</h2>
            <% if (error != null) { %>
                <div class="error-message"><%= error %></div>
            <% } %>
            <form action="login" method="post">
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary">Login</button>
            </form>
            <p class="login-note">Default: admin / admin123</p>
        </div>
    </div>
</body>
</html>

