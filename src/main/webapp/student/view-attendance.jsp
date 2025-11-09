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
    List<Attendance> attendanceList = attendanceDAO.getAttendanceByStudent(user.getId());
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Attendance - Attendance Management System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <h1>My Attendance</h1>
            <div class="nav-menu">
                <a href="dashboard.jsp">Dashboard</a>
                <a href="view-attendance.jsp">View Attendance</a>
                <a href="../logout">Logout</a>
            </div>
        </div>

        <div class="table-container">
            <h3>Attendance History</h3>
            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Subject ID</th>
                        <th>Status</th>
                        <th>Check In</th>
                        <th>Check Out</th>
                        <th>Notes</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (attendanceList.isEmpty()) { %>
                        <tr>
                            <td colspan="6" style="text-align: center;">No attendance records found.</td>
                        </tr>
                    <% } else { %>
                        <% for (Attendance att : attendanceList) { %>
                            <tr>
                                <td><%= att.getDate() %></td>
                                <td><%= att.getSubjectId() %></td>
                                <td>
                                    <span class="badge badge-<%= att.getStatus().toLowerCase() %>">
                                        <%= att.getStatus() %>
                                    </span>
                                </td>
                                <td><%= att.getCheckInTime() != null ? att.getCheckInTime() : "-" %></td>
                                <td><%= att.getCheckOutTime() != null ? att.getCheckOutTime() : "-" %></td>
                                <td><%= att.getNotes() != null ? att.getNotes() : "-" %></td>
                            </tr>
                        <% } %>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>

