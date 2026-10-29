
package Data_Access_Object;

import model.Student;
import view.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 1. Them sinh vien vao CSDL
    public boolean addStudent(Student student) {

        String sql = "INSERT INTO students "
                + "(student_id, full_name, birth_date, gender, "
                + "email, phone, address, major, class_name, gpa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.err.println("Khong ket noi duoc MySQL!");
                return false;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, student.getStudentID());
                pstmt.setString(2, student.getFullName());

                LocalDate dob = student.getDateOfBirth();

                if (dob != null) {
                    pstmt.setDate(3, java.sql.Date.valueOf(dob));
                } else {
                    pstmt.setNull(3, Types.DATE);
                }

                pstmt.setString(4, student.getGender());
                pstmt.setString(5, student.getEmail());
                pstmt.setString(6, student.getPhone());

                // Dia chi
                pstmt.setString(7, student.getAddress());

                pstmt.setString(8, student.getMajor());
                pstmt.setString(9, student.getClassName());

                // GPA
                pstmt.setDouble(10, student.getGpa());

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("Them sinh vien thanh cong!");
                    return true;
                }

                return false;
            }

        } catch (SQLException e) {
            System.err.println("===== LOI THEM SINH VIEN =====");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 2. Lay danh sach sinh vien
    public List<Student> getAllStudents() {

        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.err.println("Khong ket noi duoc MySQL!");
                return list;
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {

                    String id = rs.getString("student_id");
                    String name = rs.getString("full_name");

                    java.sql.Date dbDate = rs.getDate("birth_date");
                    LocalDate dob = (dbDate != null)
                            ? dbDate.toLocalDate()
                            : null;

                    String gender = rs.getString("gender");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    String major = rs.getString("major");
                    String className = rs.getString("class_name");

                    double gpa = rs.getDouble("gpa");

                    if (rs.wasNull()) {
                        gpa = 0.0;
                    }

                    Student student = new Student(
                            id,
                            name,
                            email,
                            dob,
                            gender,
                            phone,
                            address,
                            major,
                            className,
                            gpa
                    );

                    list.add(student);
                }
            }

        } catch (SQLException e) {
            System.err.println("===== LOI LAY DANH SACH SINH VIEN =====");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // 3. Xoa sinh vien theo MSSV
    public boolean deleteStudent(String studentId) {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.err.println("Khong ket noi duoc MySQL!");
                return false;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, studentId);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("Xoa sinh vien thanh cong!");
                    return true;
                }

                System.out.println("Khong tim thay MSSV: " + studentId);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("===== LOI XOA SINH VIEN =====");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
