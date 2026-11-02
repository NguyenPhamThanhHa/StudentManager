
package model;

import java.sql.Time;

public class Schedule {

    private int scheduleId;
    private int classId;
    private String courseId;
    private String courseName;
    private String dayName;
    private Time startTime;
    private Time endTime;
    private String room;
    private String lecturer;
    private String semester;
    private String academicYear;

    public Schedule() {
    }

    public Schedule(int scheduleId, int classId, String courseId,
                    String courseName, String dayName,
                    Time startTime, Time endTime, String room,
                    String lecturer, String semester,
                    String academicYear) {
        this.scheduleId = scheduleId;
        this.classId = classId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.dayName = dayName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.lecturer = lecturer;
        this.semester = semester;
        this.academicYear = academicYear;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
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

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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
}
