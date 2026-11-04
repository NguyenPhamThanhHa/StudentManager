
package Data_Access_Object;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Course;
import model.CourseClass;
import view.DBConnection;

public class CourseDAO {

    // =========================
    // QUẢN LÝ HỌC PHẦN
    // =========================

    public boolean addCourse(Course c) {
        String sql = """
            INSERT INTO courses
            (course_id, course_name, credits, description)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseId());
            ps.setString(2, c.getCourseName());
            ps.setInt(3, c.getCredits());
            ps.setString(4, c.getDescription());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Course(
                    rs.getString("course_id"),
                    rs.getString("course_name"),
                    rs.getInt("credits"),
                    rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateCourse(Course c) {
        String sql = """
            UPDATE courses
            SET course_name = ?, credits = ?, description = ?
            WHERE course_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getCredits());
            ps.setString(3, c.getDescription());
            ps.setString(4, c.getCourseId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(String courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courseId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Course> searchCourses(String keyword) {
        List<Course> list = new ArrayList<>();

        String sql = """
            SELECT * FROM courses
            WHERE course_id LIKE ? OR course_name LIKE ?
            ORDER BY course_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("description")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // QUẢN LÝ LỚP HỌC PHẦN
    // =========================

    public boolean addCourseClass(CourseClass c) {
        String sql = """
            INSERT INTO course_classes
            (course_id, lecturer, semester, academic_year,
             max_capacity, enrolled, status)
            VALUES (?, ?, ?, ?, ?, 0, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseId());
            ps.setString(2, c.getLecturer());
            ps.setString(3, c.getSemester());
            ps.setString(4, c.getAcademicYear());
            ps.setInt(5, c.getMaxCapacity());
            ps.setString(6, c.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CourseClass> getAllCourseClasses() {
        List<CourseClass> list = new ArrayList<>();

        String sql = """
            SELECT cc.*, c.course_name, c.credits
            FROM course_classes cc
            JOIN courses c ON cc.course_id = c.course_id
            ORDER BY cc.class_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapCourseClass(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<CourseClass> getOpenCourseClasses() {
        List<CourseClass> list = new ArrayList<>();

        String sql = """
            SELECT cc.*, c.course_name, c.credits
            FROM course_classes cc
            JOIN courses c ON cc.course_id = c.course_id
            WHERE cc.status = 'OPEN'
              AND cc.enrolled < cc.max_capacity
            ORDER BY cc.class_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapCourseClass(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateCourseClass(CourseClass c) {
        String sql = """
            UPDATE course_classes
            SET course_id = ?, lecturer = ?, semester = ?,
                academic_year = ?, max_capacity = ?, status = ?
            WHERE class_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseId());
            ps.setString(2, c.getLecturer());
            ps.setString(3, c.getSemester());
            ps.setString(4, c.getAcademicYear());
            ps.setInt(5, c.getMaxCapacity());
            ps.setString(6, c.getStatus());
            ps.setInt(7, c.getClassId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourseClass(int classId) {
        String sql = "DELETE FROM course_classes WHERE class_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CourseClass> searchCourseClasses(String keyword) {
        List<CourseClass> list = new ArrayList<>();

        String sql = """
            SELECT cc.*, c.course_name, c.credits
            FROM course_classes cc
            JOIN courses c ON cc.course_id = c.course_id
            WHERE cc.course_id LIKE ?
               OR c.course_name LIKE ?
               OR cc.lecturer LIKE ?
               OR cc.semester LIKE ?
               OR cc.academic_year LIKE ?
            ORDER BY cc.class_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";

            for (int i = 1; i <= 5; i++) {
                ps.setString(i, value);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCourseClass(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private CourseClass mapCourseClass(ResultSet rs) throws SQLException {
        return new CourseClass(
            rs.getInt("class_id"),
            rs.getString("course_id"),
            rs.getString("course_name"),
            rs.getInt("credits"),
            rs.getString("lecturer"),
            rs.getString("semester"),
            rs.getString("academic_year"),
            rs.getInt("max_capacity"),
            rs.getInt("enrolled"),
            rs.getString("status")
        );
    }
}