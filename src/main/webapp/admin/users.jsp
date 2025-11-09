<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.attendance.dao.UserDAO" %>
<%@ page import="com.attendance.model.User" %>
<%@ page import="java.util.List" %>
<%
    Object userObj = session.getAttribute("user");
    if (userObj == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    User user = (User) userObj;
    if (!"ADMIN".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
    
    UserDAO userDAO = new UserDAO();
    String role = request.getParameter("role") != null ? request.getParameter("role") : "";
    List<User> users = role.isEmpty() ? userDAO.getAllUsers() : userDAO.getUsersByRole(role);
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Users - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>Manage Users</h1>
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

        <div class="card">
            <h3>Create New User</h3>
            <form action="../admin/users/create" method="post">
                <div class="form-group">
                    <label>Username:</label>
                    <input type="text" name="username" required>
                </div>
                <div class="form-group">
                    <label>Password:</label>
                    <input type="password" name="password" required>
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" required>
                </div>
                <div class="form-group">
                    <label>Full Name:</label>
                    <input type="text" name="fullName" required>
                </div>
                <div class="form-group">
                    <label>Role:</label>
                    <select name="role" required>
                        <option value="STUDENT">Student</option>
                        <option value="TEACHER">Teacher</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Class ID (for students/teachers):</label>
                    <input type="number" name="classId">
                </div>
                <div class="form-group">
                    <label>Student ID (for students only):</label>
                    <input type="text" name="studentId">
                </div>
                <button type="submit" class="btn btn-primary">Create User</button>
            </form>
        </div>

        <div class="table-container">
            <h3>Users List</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Full Name</th>
                        <th>Role</th>
                        <th>Class ID</th>
                        <th>Student ID</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (User u : users) { %>
                        <tr>
                            <td><%= u.getId() %></td>
                            <td><%= u.getUsername() %></td>
                            <td><%= u.getEmail() %></td>
                            <td><%= u.getFullName() %></td>
                            <td><%= u.getRole() %></td>
                            <td><%= u.getClassId() %></td>
                            <td><%= u.getStudentId() != null ? u.getStudentId() : "-" %></td>
                            <td>
                                    <a href="../admin/users?action=delete&id=<%= u.getId() %>" class="btn btn-danger"
                                   onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>

