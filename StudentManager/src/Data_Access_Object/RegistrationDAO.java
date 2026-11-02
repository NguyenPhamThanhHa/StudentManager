
package Data_Access_Object;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Registration;
import view.DBConnection;

public class RegistrationDAO {

    // Sinh viên đăng ký lớp học phần
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
                // Kiểm tra lớp học phần và khóa dòng để tránh vượt sĩ số
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

                // UNIQUE(student_id, class_id) ngăn đăng ký trùng
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, studentId);
                    ps.setInt(2, classId);
                    ps.executeUpdate();
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

    // Danh sách tất cả đăng ký dành cho Admin
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

    // Danh sách đăng ký của một sinh viên
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

    // Sinh viên tự hủy lớp học phần của mình
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

    // Admin xóa một đăng ký theo mã đăng ký
    public boolean deleteRegistration(int registrationId) {

        String findSql = """
            SELECT class_id FROM registrations
            WHERE registration_id = ?
        """;

        String deleteSql = """
            DELETE FROM registrations WHERE registration_id = ?
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

// Lấy bảng điểm của một sinh viên
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
}