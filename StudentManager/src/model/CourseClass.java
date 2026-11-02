
package model;

public class CourseClass {

    private int classId;
    private String courseId;
    private String courseName;
    private int credits;
    private String lecturer;
    private String semester;
    private String academicYear;
    private int maxCapacity;
    private int enrolled;
    private String status;

    public CourseClass() {
    }

    public CourseClass(int classId, String courseId, String courseName,
                       int credits, String lecturer, String semester,
                       String academicYear, int maxCapacity,
                       int enrolled, String status) {
        this.classId = classId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.lecturer = lecturer;
        this.semester = semester;
        this.academicYear = academicYear;
        this.maxCapacity = maxCapacity;
        this.enrolled = enrolled;
        this.status = status;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return courseId + " - " + courseName + " - "
                + semester + " - " + academicYear;
    }
}
