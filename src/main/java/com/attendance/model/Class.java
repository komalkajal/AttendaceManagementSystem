package com.attendance.model;

public class Class {
    private int id;
    private String className;
    private String section;
    private int teacherId;
    private String academicYear;

    public Class() {
    }

    public Class(String className, String section, int teacherId, String academicYear) {
        this.className = className;
        this.section = section;
        this.teacherId = teacherId;
        this.academicYear = academicYear;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getDisplayName() {
        return className + " - " + section;
    }
}

