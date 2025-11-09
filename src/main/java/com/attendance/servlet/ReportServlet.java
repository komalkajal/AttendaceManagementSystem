package com.attendance.servlet;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.UserDAO;
import com.attendance.model.Attendance;
import com.attendance.model.User;
import com.attendance.util.XMLHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/reports/*")
public class ReportServlet extends HttpServlet {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
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
            String format = request.getParameter("format") != null ? request.getParameter("format") : "xml";
            String reportType = request.getParameter("type") != null ? request.getParameter("type") : "student";

            if ("STUDENT".equals(user.getRole())) {
                generateStudentReport(user.getId(), response, format);
            } else if ("TEACHER".equals(user.getRole()) || "ADMIN".equals(user.getRole())) {
                if ("class".equals(reportType)) {
                    int classId = Integer.parseInt(request.getParameter("classId"));
                    generateClassReport(classId, response, format);
                } else {
                    generateStudentReport(Integer.parseInt(request.getParameter("studentId")), response, format);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void generateStudentReport(int studentId, HttpServletResponse response, String format)
            throws SQLException, IOException, Exception {
        List<Attendance> attendanceList = attendanceDAO.getAttendanceByStudent(studentId);
        User student = userDAO.getUserById(studentId);

        if ("xml".equals(format)) {
            response.setContentType("application/xml");
            response.setHeader("Content-Disposition", "attachment; filename=attendance_report_" + studentId + ".xml");
            XMLHandler.exportAttendanceToXML(attendanceList, response.getOutputStream());
        } else if ("csv".equals(format)) {
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=attendance_report_" + studentId + ".csv");
            PrintWriter writer = response.getWriter();
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            csvPrinter.printRecord("ID", "Student ID", "Subject ID", "Class ID", "Date", "Status", "Notes", "Check In", "Check Out");
            for (Attendance attendance : attendanceList) {
                csvPrinter.printRecord(
                    attendance.getId(),
                    attendance.getStudentId(),
                    attendance.getSubjectId(),
                    attendance.getClassId(),
                    attendance.getDate(),
                    attendance.getStatus(),
                    attendance.getNotes(),
                    attendance.getCheckInTime(),
                    attendance.getCheckOutTime()
                );
            }
            csvPrinter.flush();
        }
    }

    private void generateClassReport(int classId, HttpServletResponse response, String format)
            throws SQLException, IOException {
        // Implementation for class report
        response.setContentType("text/plain");
        response.getWriter().write("Class report generation not yet implemented");
    }
}

