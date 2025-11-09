package com.attendance.dao;

import com.attendance.model.Class;
import com.attendance.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    public List<Class> getAllClasses() throws SQLException {
        List<Class> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classes.add(mapResultSetToClass(rs));
            }
        }
        return classes;
    }

    public Class getClassById(int id) throws SQLException {
        String sql = "SELECT * FROM classes WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToClass(rs);
            }
        }
        return null;
    }

    public List<Class> getClassesByTeacher(int teacherId) throws SQLException {
        List<Class> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes WHERE teacher_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classes.add(mapResultSetToClass(rs));
            }
        }
        return classes;
    }

    public boolean createClass(Class clazz) throws SQLException {
        String sql = "INSERT INTO classes (class_name, section, teacher_id, academic_year) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, clazz.getClassName());
            stmt.setString(2, clazz.getSection());
            stmt.setInt(3, clazz.getTeacherId());
            stmt.setString(4, clazz.getAcademicYear());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateClass(Class clazz) throws SQLException {
        String sql = "UPDATE classes SET class_name = ?, section = ?, teacher_id = ?, academic_year = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, clazz.getClassName());
            stmt.setString(2, clazz.getSection());
            stmt.setInt(3, clazz.getTeacherId());
            stmt.setString(4, clazz.getAcademicYear());
            stmt.setInt(5, clazz.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteClass(int id) throws SQLException {
        String sql = "DELETE FROM classes WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Class mapResultSetToClass(ResultSet rs) throws SQLException {
        Class clazz = new Class();
        clazz.setId(rs.getInt("id"));
        clazz.setClassName(rs.getString("class_name"));
        clazz.setSection(rs.getString("section"));
        clazz.setTeacherId(rs.getInt("teacher_id"));
        clazz.setAcademicYear(rs.getString("academic_year"));
        return clazz;
    }
}

