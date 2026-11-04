
package Data_Access_Object;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Student;
import view.DBConnection;

public class StudentDAO {

    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getFullName());
            ps.setDate(3, (Date) s.getBirthDate());
            ps.setString(4, s.getGender());
            ps.setString(5, s.getEmail());
            ps.setString(6, s.getPhone());
            ps.setString(7, s.getAddress());
            ps.setString(8, s.getMajor());
            ps.setString(9, s.getClassName());

            if (s.getGpa() == null) {
                ps.setNull(10, Types.DOUBLE);
            } else {
                ps.setDouble(10, s.getGpa());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapStudent(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateStudent(Student s) {
        String sql = """
            UPDATE students
            SET full_name = ?, birth_date = ?, gender = ?,
                email = ?, phone = ?, address = ?, major = ?,
                class_name = ?, gpa = ?
            WHERE student_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getFullName());
            ps.setDate(2, (Date) s.getBirthDate());
            ps.setString(3, s.getGender());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setString(6, s.getAddress());
            ps.setString(7, s.getMajor());
            ps.setString(8, s.getClassName());

            if (s.getGpa() == null) {
                ps.setNull(9, Types.DOUBLE);
            } else {
                ps.setDouble(9, s.getGpa());
            }

            ps.setString(10, s.getStudentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();

        String sql = """
            SELECT * FROM students
            WHERE student_id LIKE ?
               OR full_name LIKE ?
               OR class_name LIKE ?
            ORDER BY student_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapStudent(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Student getStudentById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        double gpaValue = rs.getDouble("gpa");
        Double gpa = rs.wasNull() ? null : gpaValue;

        return new Student(
            rs.getString("student_id"),
            rs.getString("full_name"),
            rs.getDate("birth_date"),
            rs.getString("gender"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getString("major"),
            rs.getString("class_name"),
            gpa
        );
    }
}
