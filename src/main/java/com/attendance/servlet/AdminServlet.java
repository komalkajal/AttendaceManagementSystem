package com.attendance.servlet;

import com.attendance.dao.ClassDAO;
import com.attendance.dao.SubjectDAO;
import com.attendance.dao.UserDAO;
import com.attendance.model.Class;
import com.attendance.model.Subject;
import com.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private ClassDAO classDAO = new ClassDAO();
    private SubjectDAO subjectDAO = new SubjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String path = request.getPathInfo();
        if (path == null) path = "";

        try {
            if (path.equals("/users") || path.startsWith("/users/")) {
                handleUsers(request, response, path);
            } else if (path.equals("/classes") || path.startsWith("/classes/")) {
                handleClasses(request, response, path);
            } else if (path.equals("/subjects") || path.startsWith("/subjects/")) {
                handleSubjects(request, response, path);
            } else {
                request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void handleUsers(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException, SQLException {
        if (path.equals("/users")) {
            String action = request.getParameter("action");
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                userDAO.deleteUser(id);
                response.sendRedirect("admin/users?success=User deleted");
                return;
            }
            request.setAttribute("users", userDAO.getUsersByRole(request.getParameter("role")));
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } else if (path.equals("/users/create")) {
            User user = new User();
            user.setUsername(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));
            user.setEmail(request.getParameter("email"));
            user.setFullName(request.getParameter("fullName"));
            user.setRole(request.getParameter("role"));
            if (request.getParameter("classId") != null && !request.getParameter("classId").isEmpty()) {
                user.setClassId(Integer.parseInt(request.getParameter("classId")));
            }
            user.setStudentId(request.getParameter("studentId"));
            userDAO.createUser(user);
            response.sendRedirect("../users?success=User created");
        }
    }

    private void handleClasses(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException, SQLException {
        if (path.equals("/classes")) {
            String action = request.getParameter("action");
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                classDAO.deleteClass(id);
                response.sendRedirect("admin/classes?success=Class deleted");
                return;
            }
            request.setAttribute("classes", classDAO.getAllClasses());
            request.getRequestDispatcher("/admin/classes.jsp").forward(request, response);
        } else if (path.equals("/classes/create")) {
            Class clazz = new Class();
            clazz.setClassName(request.getParameter("className"));
            clazz.setSection(request.getParameter("section"));
            clazz.setTeacherId(Integer.parseInt(request.getParameter("teacherId")));
            clazz.setAcademicYear(request.getParameter("academicYear"));
            classDAO.createClass(clazz);
            response.sendRedirect("../classes?success=Class created");
        }
    }

    private void handleSubjects(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException, SQLException {
        if (path.equals("/subjects")) {
            String action = request.getParameter("action");
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                subjectDAO.deleteSubject(id);
                response.sendRedirect("admin/subjects?success=Subject deleted");
                return;
            }
            request.setAttribute("subjects", subjectDAO.getAllSubjects());
            request.setAttribute("classes", classDAO.getAllClasses());
            request.getRequestDispatcher("/admin/subjects.jsp").forward(request, response);
        } else if (path.equals("/subjects/create")) {
            Subject subject = new Subject();
            subject.setSubjectName(request.getParameter("subjectName"));
            subject.setSubjectCode(request.getParameter("subjectCode"));
            subject.setClassId(Integer.parseInt(request.getParameter("classId")));
            subject.setTeacherId(Integer.parseInt(request.getParameter("teacherId")));
            subjectDAO.createSubject(subject);
            response.sendRedirect("../subjects?success=Subject created");
        }
    }
}

