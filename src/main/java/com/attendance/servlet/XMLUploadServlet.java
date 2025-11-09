package com.attendance.servlet;

import com.attendance.dao.AttendanceDAO;
import com.attendance.model.Attendance;
import com.attendance.util.XMLHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/upload-xml")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10) // 10MB
public class XMLUploadServlet extends HttpServlet {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            Part filePart = request.getPart("xmlFile");
            if (filePart != null && filePart.getSize() > 0) {
                List<Attendance> attendanceList = XMLHandler.parseAttendanceXML(filePart.getInputStream());
                int successCount = 0;
                for (Attendance attendance : attendanceList) {
                    if (attendanceDAO.markAttendance(attendance)) {
                        successCount++;
                    }
                }
                response.sendRedirect("teacher/dashboard.jsp?success=" + successCount + " records imported");
            } else {
                response.sendRedirect("teacher/dashboard.jsp?error=No file uploaded");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("teacher/dashboard.jsp?error=Error processing XML file: " + e.getMessage());
        }
    }
}

