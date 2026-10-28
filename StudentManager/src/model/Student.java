package model;

import java.time.LocalDate;

public class Student extends Person{
	private String StudentID;
	private String Major;
	private String ClassName;
	private double Gpa;
	
	//constructor
	public Student(String StudentID, String fullName, String email, LocalDate dateOfBirth, String gender, String phone, String address, String Major, String ClassName, double Gpa) {
		super(fullName, email, dateOfBirth, gender, phone, address);
		this.StudentID = StudentID;
		this.Major = Major;
		this.ClassName = ClassName;
		this.Gpa = Gpa;
	}

	//getter and setter
	public String getStudentID() { //mssv
		return StudentID;
	}	
	public String getMajor() { //ngành học
		return Major;
	}
	public String getClassName() { //lớp học
		return ClassName;
	}
	public double getGpa() { //điểm trung bình
		return Gpa;
	}
	
	//kế thừa thông qua super
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
	
	
	@Override
    public String getRole() {
        return "Sinh viên";

}
}
	