package com.attendance.dao;

import com.attendance.model.Attendance;
import com.attendance.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    public boolean markAttendance(Attendance attendance) throws SQLException {
        // Check if attendance already exists
        String checkSql = "SELECT id FROM attendance WHERE student_id = ? AND subject_id = ? AND date = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, attendance.getStudentId());
            checkStmt.setInt(2, attendance.getSubjectId());
            checkStmt.setDate(3, attendance.getDate());
            
            java.sql.ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                // Update existing record
                int id = rs.getInt("id");
                attendance.setId(id);
                return updateAttendance(attendance);
            } else {
                // Insert new record
                String insertSql = "INSERT INTO attendance (student_id, subject_id, class_id, date, status, notes, check_in_time, check_out_time) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, attendance.getStudentId());
                    insertStmt.setInt(2, attendance.getSubjectId());
                    insertStmt.setInt(3, attendance.getClassId());
                    insertStmt.setDate(4, attendance.getDate());
                    insertStmt.setString(5, attendance.getStatus());
                    insertStmt.setString(6, attendance.getNotes());
                    insertStmt.setString(7, attendance.getCheckInTime());
                    insertStmt.setString(8, attendance.getCheckOutTime());
                    return insertStmt.executeUpdate() > 0;
                }
            }
        }
    }

    public List<Attendance> getAttendanceByStudent(int studentId) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ? ORDER BY date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendance(rs));
            }
        }
        return attendanceList;
    }

    public List<Attendance> getAttendanceByStudentAndSubject(int studentId, int subjectId) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ? AND subject_id = ? ORDER BY date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendance(rs));
            }
        }
        return attendanceList;
    }

    public List<Attendance> getAttendanceByClassAndDate(int classId, java.sql.Date date) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE class_id = ? AND date = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendance(rs));
            }
        }
        return attendanceList;
    }

    public List<Attendance> getAttendanceByClassAndSubjectAndDate(int classId, int subjectId, java.sql.Date date) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE class_id = ? AND subject_id = ? AND date = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, subjectId);
            stmt.setDate(3, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendance(rs));
            }
        }
        return attendanceList;
    }

    public double getAttendancePercentage(int studentId, int subjectId) throws SQLException {
        String sql = "SELECT " +
                     "COUNT(CASE WHEN status IN ('PRESENT', 'LATE') THEN 1 END) * 100.0 / COUNT(*) as percentage " +
                     "FROM attendance WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("percentage");
            }
        }
        return 0.0;
    }

    public double getOverallAttendancePercentage(int studentId) throws SQLException {
        String sql = "SELECT " +
                     "COUNT(CASE WHEN status IN ('PRESENT', 'LATE') THEN 1 END) * 100.0 / COUNT(*) as percentage " +
                     "FROM attendance WHERE student_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("percentage");
            }
        }
        return 0.0;
    }

    public List<Attendance> getAttendanceByDateRange(int studentId, java.sql.Date startDate, java.sql.Date endDate) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ? AND date BETWEEN ? AND ? ORDER BY date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendance(rs));
            }
        }
        return attendanceList;
    }

    public boolean updateAttendance(Attendance attendance) throws SQLException {
        String sql = "UPDATE attendance SET status = ?, notes = ?, check_in_time = ?, check_out_time = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, attendance.getStatus());
            stmt.setString(2, attendance.getNotes());
            stmt.setString(3, attendance.getCheckInTime());
            stmt.setString(4, attendance.getCheckOutTime());
            stmt.setInt(5, attendance.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteAttendance(int id) throws SQLException {
        String sql = "DELETE FROM attendance WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setId(rs.getInt("id"));
        attendance.setStudentId(rs.getInt("student_id"));
        attendance.setSubjectId(rs.getInt("subject_id"));
        attendance.setClassId(rs.getInt("class_id"));
        attendance.setDate(rs.getDate("date"));
        attendance.setStatus(rs.getString("status"));
        attendance.setNotes(rs.getString("notes"));
        attendance.setCheckInTime(rs.getString("check_in_time"));
        attendance.setCheckOutTime(rs.getString("check_out_time"));
        return attendance;
    }
}

