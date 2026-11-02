
package model;

import java.sql.Date;

public class Registration {

    private int registrationId;
    private String studentId;
    private String fullName;
    private int classId;
    private String courseId;
    private String courseName;
    private Date registrationDate;
    private Double score;

    public Registration() {
    }

    public Registration(int registrationId, String studentId,
                        String fullName, int classId,
                        String courseId, String courseName,
                        Date registrationDate, Double score) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.fullName = fullName;
        this.classId = classId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.registrationDate = registrationDate;
        this.score = score;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
