package com.attendance.dao;

import com.attendance.model.Subject;
import com.attendance.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    public List<Subject> getAllSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjects.add(mapResultSetToSubject(rs));
            }
        }
        return subjects;
    }

    public Subject getSubjectById(int id) throws SQLException {
        String sql = "SELECT * FROM subjects WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToSubject(rs);
            }
        }
        return null;
    }

    public List<Subject> getSubjectsByClass(int classId) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE class_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjects.add(mapResultSetToSubject(rs));
            }
        }
        return subjects;
    }

    public List<Subject> getSubjectsByTeacher(int teacherId) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE teacher_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjects.add(mapResultSetToSubject(rs));
            }
        }
        return subjects;
    }

    public boolean createSubject(Subject subject) throws SQLException {
        String sql = "INSERT INTO subjects (subject_name, subject_code, class_id, teacher_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subject.getSubjectName());
            stmt.setString(2, subject.getSubjectCode());
            stmt.setInt(3, subject.getClassId());
            stmt.setInt(4, subject.getTeacherId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateSubject(Subject subject) throws SQLException {
        String sql = "UPDATE subjects SET subject_name = ?, subject_code = ?, class_id = ?, teacher_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subject.getSubjectName());
            stmt.setString(2, subject.getSubjectCode());
            stmt.setInt(3, subject.getClassId());
            stmt.setInt(4, subject.getTeacherId());
            stmt.setInt(5, subject.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteSubject(int id) throws SQLException {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Subject mapResultSetToSubject(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setId(rs.getInt("id"));
        subject.setSubjectName(rs.getString("subject_name"));
        subject.setSubjectCode(rs.getString("subject_code"));
        subject.setClassId(rs.getInt("class_id"));
        subject.setTeacherId(rs.getInt("teacher_id"));
        return subject;
    }
}

