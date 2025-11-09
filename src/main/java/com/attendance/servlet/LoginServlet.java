package com.attendance.servlet;

import com.attendance.dao.UserDAO;
import com.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDAO.authenticate(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole());

                // Redirect based on role
                switch (user.getRole()) {
                    case "ADMIN":
                        response.sendRedirect("admin/dashboard.jsp");
                        break;
                    case "TEACHER":
                        response.sendRedirect("teacher/dashboard.jsp");
                        break;
                    case "STUDENT":
                        response.sendRedirect("student/dashboard.jsp");
                        break;
                    default:
                        response.sendRedirect("login.jsp?error=Invalid role");
                }
            } else {
                response.sendRedirect("login.jsp?error=Invalid username or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Database error");
        }
    }
}

