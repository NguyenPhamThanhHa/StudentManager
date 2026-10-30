
package Data_Access_Object;

import model.Course;
import view.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Thêm học phần
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO courses "
                   + "(course_id, course_name, credits, description, lecturer, semester) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getDescription());
            pstmt.setString(5, course.getLecturer());
            pstmt.setString(6, course.getSemester());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy toàn bộ danh sách học phần
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();

        String sql = "SELECT * FROM courses";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(
                    rs.getString("course_id"),
                    rs.getString("course_name"),
                    rs.getInt("credits"),
                    rs.getString("description")
                );

                course.setLecturer(rs.getString("lecturer"));
                course.setSemester(rs.getString("semester"));

                list.add(course);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Cập nhật học phần
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET "
                   + "course_name = ?, credits = ?, description = ?, "
                   + "lecturer = ?, semester = ? "
                   + "WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseName());
            pstmt.setInt(2, course.getCredits());
            pstmt.setString(3, course.getDescription());
            pstmt.setString(4, course.getLecturer());
            pstmt.setString(5, course.getSemester());
            pstmt.setString(6, course.getCourseId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa học phần
    public boolean deleteCourse(String courseID) {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseID);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm học phần theo mã hoặc tên
    public List<Course> searchCourses(String keyword) {
        List<Course> list = new ArrayList<>();

        String sql = "SELECT * FROM courses "
                   + "WHERE course_id LIKE ? "
                   + "OR course_name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("description")
                    );

                    course.setLecturer(rs.getString("lecturer"));
                    course.setSemester(rs.getString("semester"));

                    list.add(course);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
