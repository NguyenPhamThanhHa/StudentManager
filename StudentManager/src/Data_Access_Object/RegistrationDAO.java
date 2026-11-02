
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
    // ==========================================
    public List<Registration> getAllRegistrations() {

        List<Registration> list = new ArrayList<>();

        String sql = """
            SELECT r.registration_id, r.student_id, s.full_name,
                   r.class_id, c.course_id, c.course_name,
                   r.registration_date, r.score
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
    // ==========================================
    public List<Registration> getRegistrationsByStudent(String studentId) {

        List<Registration> list = new ArrayList<>();

        String sql = """
            SELECT r.registration_id, r.student_id, s.full_name,
                   r.class_id, c.course_id, c.course_name,
                   r.registration_date, r.score
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
    // ==========================================
    public List<Registration> getRegistrationsByClass(int classId) {

        List<Registration> list = new ArrayList<>();

        String sql = """
            SELECT r.registration_id, r.student_id, s.full_name,
                   r.class_id, c.course_id, c.course_name,
                   r.registration_date, r.score
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
    // ==========================================
    public boolean updateGrade(int registrationId, double score) {

        if (!Double.isFinite(score) || score < 0 || score > 10) {
            return false;
        }

        String sql = """
            UPDATE registrations
            SET score = ?
            WHERE registration_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, score);
            ps.setInt(2, registrationId);

            return ps.executeUpdate() > 0;

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
                   r.registration_date, r.score
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
                score
        );
    }
}
