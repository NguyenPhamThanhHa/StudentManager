package model;

import java.sql.Date;

public class Student extends Person {

    private String studentId;
    private String major;
    private String className;
    private Double gpa;

    public Student(String studentId, String fullName, Date birthDate,
                   String gender, String email, String phone,
                   String address, String major, String className,
                   Double gpa) {

        super(fullName, email,
              birthDate != null ? birthDate.toLocalDate() : null,
              gender, phone, address);

        this.studentId = studentId;
        this.major = major;
        this.className = className;
        this.gpa = gpa;
    }

    // Getter và Setter ngày sinh
    public Date getBirthDate() {
        return getDateOfBirth() != null
                ? Date.valueOf(getDateOfBirth())
                : null;
    }

    public void setBirthDate(Date birthDate) {
        setDateOfBirth(
                birthDate != null ? birthDate.toLocalDate() : null
        );
    }

    // Getter và Setter mã sinh viên
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    // Getter và Setter ngành học
    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    // Getter và Setter lớp
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    // Getter và Setter GPA
    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }
}