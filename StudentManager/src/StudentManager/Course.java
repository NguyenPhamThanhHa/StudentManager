package StudentManager;


public class Course {
	private String CourseID;
	private String CourseName;
	private int Credits;
	private String Description;

	public Course (String CourseID, String CourseName, int Cerdits, String Description) {
		this.CourseID=CourseID;
		this.CourseName=CourseName;
		this.Credits=Credits;
		this.Description=Description;
	}
	public String getCourseID() {
		return CourseID;
	}
	public String CourseName() {
		return CourseName;
	}
	public int Credits() {
		return Credits;
	}
	public String Description() {
		return Description;
	}
}
