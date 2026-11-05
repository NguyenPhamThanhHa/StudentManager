
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

    
    // Điểm thành phần
    private Double midtermScore;
    private Double finalScore;
    
    // Điểm tổng kết
    private Double score;

    public Registration() {
    }

    public Registration(
            int registrationId,
            String studentId,
            String fullName,
            int classId,
            String courseId,
            String courseName,
            Date registrationDate,
            Double score
    ) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.fullName = fullName;
        this.classId = classId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.registrationDate = registrationDate;
        this.score = score;
    }

    // Constructor bổ sung điểm giữa kỳ và cuối kỳ
    public Registration(
            int registrationId,
            String studentId,
            String fullName,
            int classId,
            String courseId,
            String courseName,
            Date registrationDate,
            Double midtermScore,
            Double finalScore,
            Double score
    ) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.fullName = fullName;
        this.classId = classId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.registrationDate = registrationDate;
        this.midtermScore = midtermScore;
        this.finalScore = finalScore;
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

    public Double getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(Double midtermScore) {
        this.midtermScore = midtermScore;
    }

    public Double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    // Tính điểm tổng kết: giữa kỳ 30%, cuối kỳ 70%
    public Double calculateFinalScore() {
    if (midtermScore == null || finalScore == null) {
        return null;
    }

    return Math.round(
        (midtermScore * 0.3 + finalScore * 0.7) * 100.0
    ) / 100.0;
}

    // Xác định kết quả học phần
    public String getResult() {

        if (score == null) {
            return "Chưa có điểm";
        }

        return score >= 5.0 ? "Đạt" : "Rớt";
    }
}
