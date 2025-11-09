package com.attendance.model;

public class Subject {
    private int id;
    private String subjectName;
    private String subjectCode;
    private int classId;
    private int teacherId;

    public Subject() {
    }

    public Subject(String subjectName, String subjectCode, int classId, int teacherId) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.classId = classId;
        this.teacherId = teacherId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
}

