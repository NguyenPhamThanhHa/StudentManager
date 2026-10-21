package StudentManager;
import java.time.LocalDate;

public class Registration {
private int RegistrationID;
	private String StudentID;
	private String CourseID;
	private LocalDate RegistrationDate;
	
public Registration (int Registration, String StudentID, String CourseID, LocalDate RegistrationDate)	{
	this.RegistrationID=RegistrationID;
	this.StudentID=StudentID;
	this.CourseID=CourseID;
	this.RegistrationDate=RegistrationDate;
}

	public int getRegistrationID() {
	return RegistrationID;
}
	
	public String getStudentID() {
	return StudentID;
}
	
	public String getCourseID() {
	return CourseID;
}
	
	public LocalDate getRegistrationDate() {
	return RegistrationDate;
}
}
