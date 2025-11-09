<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.attendance.dao.ClassDAO" %>
<%@ page import="com.attendance.dao.UserDAO" %>
<%@ page import="com.attendance.model.*" %>
<%@ page import="java.util.*" %>
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
    
    ClassDAO classDAO = new ClassDAO();
    UserDAO userDAO = new UserDAO();
    List<Class> classes = classDAO.getAllClasses();
    List<User> teachers = userDAO.getUsersByRole("TEACHER");
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Classes - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>Manage Classes</h1>
            <div class="nav-menu">
                <a href="dashboard.jsp">Dashboard</a>
                <a href="users.jsp">Users</a>
                <a href="classes.jsp">Classes</a>
                <a href="subjects.jsp">Subjects</a>
                <a href="../logout">Logout</a>
            </div>
        </div>

        <% if (success != null) { %>
            <div class="success-message"><%= success %></div>
        <% } %>

        <div class="card">
            <h3>Create New Class</h3>
            <form action="../admin/classes/create" method="post">
                <div class="form-group">
                    <label>Class Name:</label>
                    <input type="text" name="className" required>
                </div>
                <div class="form-group">
                    <label>Section:</label>
                    <input type="text" name="section" required>
                </div>
                <div class="form-group">
                    <label>Teacher:</label>
                    <select name="teacherId" required>
                        <option value="">Select Teacher</option>
                        <% for (User teacher : teachers) { %>
                            <option value="<%= teacher.getId() %>"><%= teacher.getFullName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label>Academic Year:</label>
                    <input type="text" name="academicYear" placeholder="e.g., 2024-2025" required>
                </div>
                <button type="submit" class="btn btn-primary">Create Class</button>
            </form>
        </div>

        <div class="table-container">
            <h3>Classes List</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Class Name</th>
                        <th>Section</th>
                        <th>Teacher ID</th>
                        <th>Academic Year</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (classes.isEmpty()) { %>
                        <tr>
                            <td colspan="6" style="text-align: center;">No classes found.</td>
                        </tr>
                    <% } else { %>
                        <% for (Class clazz : classes) { %>
                            <tr>
                                <td><%= clazz.getId() %></td>
                                <td><%= clazz.getClassName() %></td>
                                <td><%= clazz.getSection() %></td>
                                <td><%= clazz.getTeacherId() %></td>
                                <td><%= clazz.getAcademicYear() %></td>
                                <td>
                                    <a href="../admin/classes?action=delete&id=<%= clazz.getId() %>" class="btn btn-danger" 
                                       onclick="return confirm('Are you sure you want to delete this class?')">Delete</a>
                                </td>
                            </tr>
                        <% } %>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>

