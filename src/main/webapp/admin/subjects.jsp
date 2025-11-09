<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.attendance.dao.SubjectDAO" %>
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
    
    SubjectDAO subjectDAO = new SubjectDAO();
    ClassDAO classDAO = new ClassDAO();
    UserDAO userDAO = new UserDAO();
    List<Subject> subjects = subjectDAO.getAllSubjects();
    List<Class> classes = classDAO.getAllClasses();
    List<User> teachers = userDAO.getUsersByRole("TEACHER");
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Subjects - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>Manage Subjects</h1>
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
            <h3>Create New Subject</h3>
            <form action="../admin/subjects/create" method="post">
                <div class="form-group">
                    <label>Subject Name:</label>
                    <input type="text" name="subjectName" required>
                </div>
                <div class="form-group">
                    <label>Subject Code:</label>
                    <input type="text" name="subjectCode" required>
                </div>
                <div class="form-group">
                    <label>Class:</label>
                    <select name="classId" required>
                        <option value="">Select Class</option>
                        <% for (Class clazz : classes) { %>
                            <option value="<%= clazz.getId() %>"><%= clazz.getDisplayName() %></option>
                        <% } %>
                    </select>
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
                <button type="submit" class="btn btn-primary">Create Subject</button>
            </form>
        </div>

        <div class="table-container">
            <h3>Subjects List</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Subject Name</th>
                        <th>Subject Code</th>
                        <th>Class ID</th>
                        <th>Teacher ID</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (subjects.isEmpty()) { %>
                        <tr>
                            <td colspan="6" style="text-align: center;">No subjects found.</td>
                        </tr>
                    <% } else { %>
                        <% for (Subject subject : subjects) { %>
                            <tr>
                                <td><%= subject.getId() %></td>
                                <td><%= subject.getSubjectName() %></td>
                                <td><%= subject.getSubjectCode() %></td>
                                <td><%= subject.getClassId() %></td>
                                <td><%= subject.getTeacherId() %></td>
                                <td>
                                    <a href="../admin/subjects?action=delete&id=<%= subject.getId() %>" class="btn btn-danger" 
                                       onclick="return confirm('Are you sure you want to delete this subject?')">Delete</a>
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

