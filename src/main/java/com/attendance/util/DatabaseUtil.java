package com.attendance.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String DB_URL = System.getenv("JDBC_DATABASE_URL") != null ? 
            System.getenv("JDBC_DATABASE_URL") : 
            "jdbc:h2:mem:attendance;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String DB_USER = System.getenv("JDBC_DATABASE_USERNAME") != null ? 
            System.getenv("JDBC_DATABASE_USERNAME") : "sa";
    private static final String DB_PASSWORD = System.getenv("JDBC_DATABASE_PASSWORD") != null ? 
            System.getenv("JDBC_DATABASE_PASSWORD") : "";

    static {
        try {
            // Load H2 driver for development, MySQL for production
            if (DB_URL.contains("h2")) {
                Class.forName("org.h2.Driver");
            } else {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            initializeDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void initializeDatabase() throws SQLException {
        boolean isH2 = DB_URL.contains("h2");
        try (Connection conn = getConnection()) {
            // Create users table
            String usersTable = isH2 ?
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "role VARCHAR(20) NOT NULL, " +
                "class_id INT, " +
                "student_id VARCHAR(50)" +
                ")" :
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "role VARCHAR(20) NOT NULL, " +
                "class_id INT, " +
                "student_id VARCHAR(50)" +
                ")";
            conn.createStatement().execute(usersTable);

            // Create classes table
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS classes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "class_name VARCHAR(50) NOT NULL, " +
                "section VARCHAR(10) NOT NULL, " +
                "teacher_id INT, " +
                "academic_year VARCHAR(20)" +
                ")"
            );

            // Create subjects table
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS subjects (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "subject_name VARCHAR(100) NOT NULL, " +
                "subject_code VARCHAR(20) NOT NULL, " +
                "class_id INT NOT NULL, " +
                "teacher_id INT NOT NULL" +
                ")"
            );

            // Create attendance table
            String attendanceTable = isH2 ?
                "CREATE TABLE IF NOT EXISTS attendance (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "student_id INT NOT NULL, " +
                "subject_id INT NOT NULL, " +
                "class_id INT NOT NULL, " +
                "date DATE NOT NULL, " +
                "status VARCHAR(20) NOT NULL, " +
                "notes TEXT, " +
                "check_in_time TIME, " +
                "check_out_time TIME, " +
                "CONSTRAINT unique_attendance UNIQUE(student_id, subject_id, date)" +
                ")" :
                "CREATE TABLE IF NOT EXISTS attendance (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "student_id INT NOT NULL, " +
                "subject_id INT NOT NULL, " +
                "class_id INT NOT NULL, " +
                "date DATE NOT NULL, " +
                "status VARCHAR(20) NOT NULL, " +
                "notes TEXT, " +
                "check_in_time TIME, " +
                "check_out_time TIME, " +
                "UNIQUE(student_id, subject_id, date)" +
                ")";
            conn.createStatement().execute(attendanceTable);

            // Insert default admin user if not exists
            String adminInsert = isH2 ?
                "MERGE INTO users (username, password, email, full_name, role) " +
                "KEY(username) " +
                "VALUES ('admin', 'admin123', 'admin@school.com', 'Administrator', 'ADMIN')" :
                "INSERT INTO users (username, password, email, full_name, role) " +
                "SELECT 'admin', 'admin123', 'admin@school.com', 'Administrator', 'ADMIN' " +
                "WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin')";
            try {
                conn.createStatement().execute(adminInsert);
            } catch (SQLException e) {
                // Ignore if admin already exists
                if (!e.getMessage().contains("already exists") && !e.getMessage().contains("Duplicate")) {
                    throw e;
                }
            }
        }
    }
}

