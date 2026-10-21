package StudentManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    // Thông tin kết nối Database của bạn (Hãy thay đổi cho khớp với XAMPP/MySQL local)
    private final String URL = "jdbc:mysql://localhost:3306/ten_database_cua_ban?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASSWORD = ""; // Mật khẩu XAMPP/Wamp của bạn (nếu có)

    // Hàm kết nối cơ sở dữ liệu
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 1. Lấy toàn bộ danh sách sinh viên (Dùng để hiển thị lên JTable)
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String studentID = rs.getString("studentID");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                
                // Xử lý ngày tháng an toàn với LocalDate
                Date dbDate = rs.getDate("dateOfBirth");
                LocalDate dateOfBirth = (dbDate != null) ? dbDate.toLocalDate() : null;
                
                String gender = rs.getString("gender");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String major = rs.getString("major");
                String className = rs.getString("className");
                double gpa = rs.getDouble("gpa");

                // Khởi tạo đối tượng Student thông qua Constructor đã viết
                Student student = new Student(studentID, fullName, email, dateOfBirth, gender, phone, address, major, className, gpa);
                list.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm sinh viên mới vào Database
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (studentID, fullName, email, dateOfBirth, gender, phone, address, major, className, gpa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentID());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setDate(4, student.getDateOfBirth() != null ? Date.valueOf(student.getDateOfBirth()) : null);
            pstmt.setString(5, student.getGender());
            pstmt.setString(6, student.getPhone());
            pstmt.setString(7, student.getAddress());
            pstmt.setString(8, student.getMajor());
            pstmt.setString(9, student.getClassName());
            pstmt.setDouble(10, student.getGpa());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Xóa sinh viên theo Mã sinh viên (studentID)
    public boolean deleteStudent(String studentID) {
        String sql = "DELETE FROM students WHERE studentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}