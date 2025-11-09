package com.attendance.servlet;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.ClassDAO;
import com.attendance.dao.SubjectDAO;
import com.attendance.dao.UserDAO;
import com.attendance.model.Attendance;
import com.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/attendance/*")
public class AttendanceServlet extends HttpServlet {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private ClassDAO classDAO = new ClassDAO();
    private SubjectDAO subjectDAO = new SubjectDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login.jsp");
            return;
        }

        String path = request.getPathInfo();
        if (path == null) path = "";

        try {
            User user = (User) session.getAttribute("user");
            String role = user.getRole();

            if (path.equals("/mark") || path.equals("")) {
                // Show attendance marking page
                if ("TEACHER".equals(role) || "ADMIN".equals(role)) {
                    int classId = request.getParameter("classId") != null ?
                            Integer.parseInt(request.getParameter("classId")) : 0;
                    int subjectId = request.getParameter("subjectId") != null ?
                            Integer.parseInt(request.getParameter("subjectId")) : 0;
                    String dateStr = request.getParameter("date");
                    Date date = dateStr != null ? Date.valueOf(dateStr) : new Date(System.currentTimeMillis());

                    if (classId > 0 && subjectId > 0) {
                        List<User> students = userDAO.getStudentsByClass(classId);
                        List<Attendance> existingAttendance = attendanceDAO.getAttendanceByClassAndSubjectAndDate(
                                classId, subjectId, date);

                        request.setAttribute("students", students);
                        request.setAttribute("classId", classId);
                        request.setAttribute("subjectId", subjectId);
                        request.setAttribute("date", date);
                        request.setAttribute("existingAttendance", existingAttendance);
                    }

                    request.setAttribute("classes", classDAO.getAllClasses());
                    request.setAttribute("subjects", subjectDAO.getAllSubjects());
                    request.getRequestDispatcher("/teacher/mark-attendance.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } else if (path.equals("/view")) {
                // View attendance records
                if ("STUDENT".equals(role)) {
                    List<Attendance> attendanceList = attendanceDAO.getAttendanceByStudent(user.getId());
                    request.setAttribute("attendanceList", attendanceList);
                    request.getRequestDispatcher("/student/view-attendance.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login.jsp");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            if (!"TEACHER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            int classId = Integer.parseInt(request.getParameter("classId"));
            int subjectId = Integer.parseInt(request.getParameter("subjectId"));
            Date date = Date.valueOf(request.getParameter("date"));

            String[] studentIds = request.getParameterValues("studentId");
            String[] statuses = request.getParameterValues("status");
            String[] notes = request.getParameterValues("notes");

            if (studentIds != null && statuses != null) {
                for (int i = 0; i < studentIds.length; i++) {
                    Attendance attendance = new Attendance();
                    attendance.setStudentId(Integer.parseInt(studentIds[i]));
                    attendance.setSubjectId(subjectId);
                    attendance.setClassId(classId);
                    attendance.setDate(date);
                    attendance.setStatus(statuses[i]);
                    attendance.setNotes(notes != null && i < notes.length ? notes[i] : null);

                    if ("PRESENT".equals(statuses[i])) {
                        attendance.setCheckInTime(LocalTime.now().toString());
                    }

                    attendanceDAO.markAttendance(attendance);
                }
            }

            response.sendRedirect("attendance/mark?classId=" + classId + "&subjectId=" + subjectId + "&date=" + date + "&success=true");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

