package model;

public class Course {

    private String courseId;
    private String courseName;
    private int credits;
    private String description;
    private String semester;
    private int maxStudents;
    private String lecturer;

    // Constructor
    public Course(String courseId, String courseName, int credits,
                  String description, String semester,
                  int maxStudents) {

        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
        this.semester = semester;
        this.maxStudents = maxStudents;
    }

    // Constructor 4 tham số
    public Course(String courseId, String courseName,
                  int credits, String description) {

        this(courseId, courseName, credits,
             description, null, 0);
    }

    // Getters và Setters
    public String getCourseId() {
        return courseId;
    }

    public String getCourseID() {
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }
}