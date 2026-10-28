package model;

public class Registration {
    private String registrationID;
    private String studentId;
    private String courseId;

    public Registration() {}

    public Registration(String registrationID, String studentId, String courseId) {
        this.registrationID = registrationID; // Đã sửa: dùng this. để gán đúng biến
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Getters và Setters
    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}