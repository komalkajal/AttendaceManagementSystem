<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.attendance.dao.ClassDAO" %>
<%@ page import="com.attendance.dao.SubjectDAO" %>
<%@ page import="com.attendance.dao.UserDAO" %>
<%@ page import="com.attendance.dao.AttendanceDAO" %>
<%@ page import="com.attendance.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.Date" %>
<%
    Object userObj = session.getAttribute("user");
    if (userObj == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    User user = (User) userObj;
    if (!"TEACHER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
    
    ClassDAO classDAO = new ClassDAO();
    SubjectDAO subjectDAO = new SubjectDAO();
    UserDAO userDAO = new UserDAO();
    AttendanceDAO attendanceDAO = new AttendanceDAO();
    
    List<Class> classes = classDAO.getAllClasses();
    List<Subject> subjects = subjectDAO.getAllSubjects();
    
    int classId = request.getParameter("classId") != null ? Integer.parseInt(request.getParameter("classId")) : 0;
    int subjectId = request.getParameter("subjectId") != null ? Integer.parseInt(request.getParameter("subjectId")) : 0;
    String dateStr = request.getParameter("date");
    Date date = dateStr != null ? Date.valueOf(dateStr) : new Date(System.currentTimeMillis());
    
    List<User> students = new ArrayList<>();
    Map<Integer, Attendance> existingAttendanceMap = new HashMap<>();
    
    if (classId > 0 && subjectId > 0) {
        students = userDAO.getStudentsByClass(classId);
        List<Attendance> existingAttendance = attendanceDAO.getAttendanceByClassAndSubjectAndDate(classId, subjectId, date);
        for (Attendance att : existingAttendance) {
            existingAttendanceMap.put(att.getStudentId(), att);
        }
    }
    
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Mark Attendance - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>Mark Attendance</h1>
            <div class="nav-menu">
                <a href="dashboard.jsp">Dashboard</a>
                <a href="../attendance/mark">Mark Attendance</a>
                <a href="../upload-xml">Upload XML</a>
                <a href="../logout">Logout</a>
            </div>
        </div>

        <% if (success != null) { %>
            <div class="success-message">Attendance marked successfully!</div>
        <% } %>

        <div class="card">
            <h3>Select Class, Subject, and Date</h3>
            <form method="get" action="../attendance/mark">
                <div class="form-group">
                    <label>Class:</label>
                    <select name="classId" required onchange="this.form.submit()">
                        <option value="">Select Class</option>
                        <% for (Class clazz : classes) { %>
                            <option value="<%= clazz.getId() %>" <%= clazz.getId() == classId ? "selected" : "" %>>
                                <%= clazz.getDisplayName() %>
                            </option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label>Subject:</label>
                    <select name="subjectId" required onchange="this.form.submit()">
                        <option value="">Select Subject</option>
                        <% for (Subject subject : subjects) { %>
                            <option value="<%= subject.getId() %>" <%= subject.getId() == subjectId ? "selected" : "" %>>
                                <%= subject.getSubjectName() %> (<%= subject.getSubjectCode() %>)
                            </option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label>Date:</label>
                    <input type="date" name="date" value="<%= dateStr != null ? dateStr : new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" required onchange="this.form.submit()">
                </div>
            </form>
        </div>

        <% if (classId > 0 && subjectId > 0 && !students.isEmpty()) { %>
            <div class="card">
                <h3>Mark Attendance for Students</h3>
                <form method="post" action="../attendance/mark">
                    <input type="hidden" name="classId" value="<%= classId %>">
                    <input type="hidden" name="subjectId" value="<%= subjectId %>">
                    <input type="hidden" name="date" value="<%= dateStr %>">
                    
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Student ID</th>
                                    <th>Name</th>
                                    <th>Status</th>
                                    <th>Notes</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (User student : students) { 
                                    Attendance existing = existingAttendanceMap.get(student.getId());
                                %>
                                    <tr>
                                        <td><%= student.getStudentId() != null ? student.getStudentId() : student.getId() %></td>
                                        <td><%= student.getFullName() %></td>
                                        <td>
                                            <input type="hidden" name="studentId" value="<%= student.getId() %>">
                                            <select name="status" required>
                                                <option value="PRESENT" <%= existing != null && "PRESENT".equals(existing.getStatus()) ? "selected" : "" %>>Present</option>
                                                <option value="ABSENT" <%= existing != null && "ABSENT".equals(existing.getStatus()) ? "selected" : "" %>>Absent</option>
                                                <option value="LATE" <%= existing != null && "LATE".equals(existing.getStatus()) ? "selected" : "" %>>Late</option>
                                                <option value="EXCUSED" <%= existing != null && "EXCUSED".equals(existing.getStatus()) ? "selected" : "" %>>Excused</option>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="text" name="notes" value="<%= existing != null && existing.getNotes() != null ? existing.getNotes() : "" %>" placeholder="Optional notes">
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    <button type="submit" class="btn btn-primary">Save Attendance</button>
                </form>
            </div>
        <% } %>
    </div>
</body>
</html>

