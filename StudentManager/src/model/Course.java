package model;

public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private String description;

    public Course() {}

    public Course(String courseId, String courseName, int credits, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
    }

    // Getters
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public String getDescription() { return description; }

    // Setters
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setDescription(String description) { this.description = description; }
}