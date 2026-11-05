
package Data_Access_Object;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Registration;
import view.DBConnection;

public class RegistrationDAO {

    // ==========================================
    // 1. SINH VIÊN ĐĂNG KÝ LỚP HỌC PHẦN
    // ==========================================
    public boolean registerClass(String studentId, int classId) {

        String checkSql = """
            SELECT status, enrolled, max_capacity
            FROM course_classes
            WHERE class_id = ?
            FOR UPDATE
        """;

        String insertSql = """
            INSERT INTO registrations
            (student_id, class_id, registration_date)
            VALUES (?, ?, CURDATE())
        """;

        String updateSql = """
            UPDATE course_classes
            SET enrolled = enrolled + 1
            WHERE class_id = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {

                // Kiểm tra lớp học phần
                try (PreparedStatement ps = conn.prepareStatement(checkSql)) {

                    ps.setInt(1, classId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()
                                || !"OPEN".equals(rs.getString("status"))
                                || rs.getInt("enrolled") >= rs.getInt("max_capacity")) {

                            conn.rollback();
                            return false;
                        }
                    }
                }

                // Thêm đăng ký
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {

                    ps.setString(1, studentId);
                    ps.setInt(2, classId);

                    ps.executeUpdate();
                }

                // Cập nhật sĩ số
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {

                    ps.setInt(1, classId);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {

                conn.rollback();
                e.printStackTrace();
                return false;

            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 2. ADMIN XEM TẤT CẢ ĐĂNG KÝ
    // Bổ sung midterm_score, final_score
    // ==========================================
    public List<Registration> getAllRegistrations() {

        List<Registration> list = new ArrayList<>();

        String sql = """
            SELECT r.registration_id, r.student_id, s.full_name,
                   r.class_id, c.course_id, c.course_name,
                   r.registration_date,
                   r.midterm_score, r.final_score, r.score
            FROM registrations r
            JOIN students s ON r.student_id = s.student_id
            JOIN course_classes cc ON r.class_id = cc.class_id
            JOIN courses c ON cc.course_id = c.course_id
            ORDER BY r.registration_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRegistration(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==========================================
    // 3. SINH VIÊN XEM LỊCH SỬ ĐĂNG KÝ
    // Bổ sung hai đầu điểm
    // ==========================================
    public List<Registration> getRegistrationsByStudent(String studentId) {

        List<Registration> list = new ArrayList<>();

        String sql = """
            SELECT r.registration_id, r.student_id, s.full_name,
                   r.class_id, c.course_id, c.course_name,
                   r.registration_date,
                   r.midterm_score, r.final_score, r.score
            FROM registrations r
            JOIN students s ON r.student_id = s.student_id
            JOIN course_classes cc ON r.class_id = cc.class_id
            JOIN courses c ON cc.course_id = c.course_id
            WHERE r.student_id = ?
            ORDER BY r.registration_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapRegistration(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==========================================
    // 4. SINH VIÊN HỦY ĐĂNG KÝ
    // ==========================================
    public boolean unregisterClass(String studentId, int classId) {

        String deleteSql = """
            DELETE FROM registrations
            WHERE student_id = ? AND class_id = ?
        """;

        String updateSql = """
            UPDATE course_classes
            SET enrolled = GREATEST(enrolled - 1, 0)
            WHERE class_id = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {

                int deleted;

                try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {

                    ps.setString(1, studentId);
                    ps.setInt(2, classId);

                    deleted = ps.executeUpdate();
                }

                if (deleted == 0) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {

                    ps.setInt(1, classId);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {

                conn.rollback();
                e.printStackTrace();
                return false;

            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 5. ADMIN XÓA ĐĂNG KÝ
    // ==========================================
    public boolean deleteRegistration(int registrationId) {

        String findSql = """
            SELECT class_id
            FROM registrations
            WHERE registration_id = ?
        """;

        String deleteSql = """
            DELETE FROM registrations
            WHERE registration_id = ?
        """;

        String updateSql = """
            UPDATE course_classes
            SET enrolled = GREATEST(enrolled - 1, 0)
            WHERE class_id = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {

                int classId;

                try (PreparedStatement ps = conn.prepareStatement(findSql)) {

                    ps.setInt(1, registrationId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        classId = rs.getInt("class_id");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {

                    ps.setInt(1, registrationId);

                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {

                    ps.setInt(1, classId);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {

                conn.rollback();
                e.printStackTrace();
                return false;

            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 6. ADMIN LẤY DANH SÁCH SINH VIÊN THEO LỚP
    // Lấy thêm điểm giữa kỳ và cuối kỳ
    // ==========================================
    public List<Registration> getRegistrationsByClass(int classId) {

        List<Registration> list = new ArrayList<>();

        String sql = """
            SELECT r.registration_id, r.student_id, s.full_name,
                   r.class_id, c.course_id, c.course_name,
                   r.registration_date,
                   r.midterm_score, r.final_score, r.score
            FROM registrations r
            JOIN students s ON r.student_id = s.student_id
            JOIN course_classes cc ON r.class_id = cc.class_id
            JOIN courses c ON cc.course_id = c.course_id
            WHERE r.class_id = ?
            ORDER BY r.student_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapRegistration(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==========================================
    // 7. ADMIN NHẬP / CẬP NHẬT ĐIỂM
    // Nhập điểm giữa kỳ và cuối kỳ
    // Hệ thống tự tính điểm tổng kết
    // ==========================================
    public boolean updateGrade(
            int registrationId,
            double midtermScore,
            double finalExamScore) {

        // Kiểm tra điểm hợp lệ
        if (!Double.isFinite(midtermScore)
                || !Double.isFinite(finalExamScore)
                || midtermScore < 0 || midtermScore > 10
                || finalExamScore < 0 || finalExamScore > 10) {

            return false;
        }

        // Công thức tính tổng kết: 30% giữa kỳ, 70% cuối kỳ
        double totalScore = Math.round(
                (midtermScore * 0.3 + finalExamScore * 0.7) * 100.0
        ) / 100.0;

        String updateGradeSql = """
            UPDATE registrations
            SET midterm_score = ?,
                final_score = ?,
                score = ?
            WHERE registration_id = ?
        """;

        // Lấy mã sinh viên sau khi cập nhật điểm
        String findStudentSql = """
            SELECT student_id
            FROM registrations
            WHERE registration_id = ?
        """;

        // Lấy GPA đã tính từ view
        String getGpaSql = """
            SELECT gpa
            FROM v_student_gpa
            WHERE student_id = ?
        """;

        // Cập nhật GPA vào bảng students
        String updateGpaSql = """
            UPDATE students
            SET gpa = ?
            WHERE student_id = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {

                // Cập nhật hai đầu điểm và điểm tổng kết
                try (PreparedStatement ps =
                             conn.prepareStatement(updateGradeSql)) {

                    ps.setDouble(1, midtermScore);
                    ps.setDouble(2, finalExamScore);
                    ps.setDouble(3, totalScore);
                    ps.setInt(4, registrationId);

                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }

                //Tìm sinh viên có điểm vừa cập nhật
                String studentId;

                try (PreparedStatement ps =
                             conn.prepareStatement(findStudentSql)) {

                    ps.setInt(1, registrationId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        studentId = rs.getString("student_id");
                    }
                }

                // Lấy GPA mới từ view
                double gpa = 0.0;

                try (PreparedStatement ps =
                             conn.prepareStatement(getGpaSql)) {

                    ps.setString(1, studentId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (rs.next()) {
                            gpa = rs.getDouble("gpa");
                        }
                    }
                }

                // Lưu GPA vào bảng students
                try (PreparedStatement ps =
                             conn.prepareStatement(updateGpaSql)) {

                    ps.setDouble(1, gpa);
                    ps.setString(2, studentId);

                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {

                conn.rollback();
                e.printStackTrace();
                return false;

            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 8. SINH VIÊN XEM BẢNG ĐIỂM
    // ==========================================
    public List<Registration> getGradesByStudent(String studentId) {

        List<Registration> list = new ArrayList<>();

        String sql = """
            SELECT r.registration_id, r.student_id, s.full_name,
                   r.class_id, c.course_id, c.course_name,
                   r.registration_date,
                   r.midterm_score, r.final_score, r.score
            FROM registrations r
            JOIN students s ON r.student_id = s.student_id
            JOIN course_classes cc ON r.class_id = cc.class_id
            JOIN courses c ON cc.course_id = c.course_id
            WHERE r.student_id = ?
            ORDER BY c.course_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapRegistration(rs));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return list;
    }

    // ==========================================
    // 9. CHUYỂN DỮ LIỆU SQL THÀNH REGISTRATION
    // ==========================================
    private Registration mapRegistration(ResultSet rs)
            throws SQLException {

        double midtermValue = rs.getDouble("midterm_score");
        Double midtermScore = rs.wasNull() ? null : midtermValue;

        double finalValue = rs.getDouble("final_score");
        Double finalScore = rs.wasNull() ? null : finalValue;

        double scoreValue = rs.getDouble("score");
        Double score = rs.wasNull() ? null : scoreValue;

        return new Registration(
                rs.getInt("registration_id"),
                rs.getString("student_id"),
                rs.getString("full_name"),
                rs.getInt("class_id"),
                rs.getString("course_id"),
                rs.getString("course_name"),
                rs.getDate("registration_date"),
                midtermScore,
                finalScore,
                score
        );
    }
}
