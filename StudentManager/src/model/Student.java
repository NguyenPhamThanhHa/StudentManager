package model;

import java.time.LocalDate;

public class Student extends Person {
    private String StudentID;
    private String Major;
    private String ClassName;
    private double Gpa;
    
    // Constructor
    public Student(String StudentID, String fullName, String email, LocalDate dateOfBirth, String gender, String phone, String address, String Major, String ClassName, double Gpa) {
        super(fullName, email, dateOfBirth, gender, phone, address);
        this.StudentID = StudentID;
        this.Major = Major;
        this.ClassName = ClassName;
        this.Gpa = Gpa;
    }

    // --- Getter ---
    public String getStudentID() { 
        return StudentID;
    }   
    public String getMajor() { 
        return Major;
    }
    public String getClassName() { 
        return ClassName;
    }
    public double getGpa() { 
        return Gpa;
    }
    
    // --- Setter ---
    public void setStudentID(String studentID) {
        StudentID = studentID;
    }
    public void setMajor(String major) {
        Major = major;
    }
    public void setClassName(String className) {
        ClassName = className;
    }
    public void setGpa(double gpa) {
        Gpa = gpa;
    }

    // Kế thừa thông qua super (Getter từ lớp Person)
    public String getFullName() {
        return super.getFullName();
    }
    public String getEmail() {
        return super.getEmail();
    }
    public LocalDate getDateOfBirth() { 
        return super.getDateOfBirth();
    }
    public String getGender() { 
        return super.getGender();
    }
    public String getPhone() { 
        return super.getPhone();
    }
    public String getAddress() {
        return super.getAddress();
    }

    // Kế thừa thông qua super (Setter từ lớp Person nếu cần)
    public void setFullName(String fullName) {
        super.setFullName(fullName);
    }
    public void setEmail(String email) {
        super.setEmail(email);
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        super.setDateOfBirth(dateOfBirth);
    }
    public void setGender(String gender) {
        super.setGender(gender);
    }
    public void setPhone(String phone) {
        super.setPhone(phone);
    }
    public void setAddress(String address) {
        super.setAddress(address);
    }

    @Override
    public String getRole() {
        return "Sinh viên";
    }
}