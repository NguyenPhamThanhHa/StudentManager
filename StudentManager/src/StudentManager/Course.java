package StudentManager;

public class Course {
	private String CourseID;
	private String CourseName;
	private int Cerdits;
	private String Description;

	public Course (String CourseID, String CourseName, int Cerdits, String Description) {
		this.CourseID=CourseID;
		this.CourseName=CourseName;
		this.Cerdits=Cerdits;
		this.Description=Description;
	}
	public String getCourseID() {
		return CourseID;
	}
	public String CourseName() {
		return CourseName;
	}
	public int Cerdits() {
		return Cerdits;
	}
	public String Description() {
		return Description;
	}
}
