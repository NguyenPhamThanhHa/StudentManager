package Data_Access_Object;

// Khuyên nên chuyển DBConnection sang package util hoặc database thay vì view
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import view.DBConnection; // Giả sử bạn đã đổi package cho DBConnection

public class RegistrationDAO {

    /**
     * Thực hiện đăng ký học phần cho sinh viên
     * @param studentId Mã sinh viên
     * @param courseId Mã học phần
     * @return true nếu đăng ký thành công, false nếu thất bại
     */
    public boolean registerCourse(String studentId, String courseId) {
        String sql = "INSERT INTO registrations (student_id, course_id) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            // Kiểm tra lỗi trùng khóa chính / unique constraint (Ví dụ mã lỗi SQLState '23000' hoặc tùy loại DB như MySQL/SQL Server)
            if ("23000".equals(e.getSQLState())) {
                System.err.println("Lỗi: Sinh viên này đã đăng ký học phần này rồi!");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}