
package Data_Access_Object;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Schedule;
import view.DBConnection;

public class ScheduleDAO {

    // Lấy tất cả lịch học dành cho Admin
    public List<Schedule> getAllSchedules() {
        List<Schedule> list = new ArrayList<>();

        String sql = """
            SELECT sc.schedule_id, sc.class_id,
                   c.course_id, c.course_name,
                   sc.day_of_week, sc.start_time,
                   sc.end_time, sc.room,
                   cc.lecturer, cc.semester, cc.academic_year
            FROM schedules sc
            JOIN course_classes cc ON sc.class_id = cc.class_id
            JOIN courses c ON cc.course_id = c.course_id
            ORDER BY sc.day_of_week, sc.start_time
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapSchedule(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy thời khóa biểu của một sinh viên
    public List<Schedule> getStudentSchedules(String studentId) {
        List<Schedule> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT sc.schedule_id, sc.class_id,
                   c.course_id, c.course_name,
                   sc.day_of_week, sc.start_time,
                   sc.end_time, sc.room,
                   cc.lecturer, cc.semester, cc.academic_year
            FROM registrations r
            JOIN course_classes cc ON r.class_id = cc.class_id
            JOIN schedules sc ON cc.class_id = sc.class_id
            JOIN courses c ON cc.course_id = c.course_id
            WHERE r.student_id = ?
            ORDER BY sc.day_of_week, sc.start_time
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSchedule(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm lịch học
    public boolean addSchedule(Schedule s) {
        String sql = """
            INSERT INTO schedules
            (class_id, day_of_week, start_time, end_time, room)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, s.getClassId());
            ps.setString(2, s.getDayName());
            ps.setTime(3, s.getStartTime());
            ps.setTime(4, s.getEndTime());
            ps.setString(5, s.getRoom());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật lịch học
    public boolean updateSchedule(Schedule s) {
        String sql = """
            UPDATE schedules
            SET class_id = ?, day_of_week = ?,
                start_time = ?, end_time = ?, room = ?
            WHERE schedule_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, s.getClassId());
            ps.setString(2, s.getDayName());
            ps.setTime(3, s.getStartTime());
            ps.setTime(4, s.getEndTime());
            ps.setString(5, s.getRoom());
            ps.setInt(6, s.getScheduleId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa lịch học
    public boolean deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM schedules WHERE schedule_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Schedule mapSchedule(ResultSet rs) throws SQLException {
        return new Schedule(
            rs.getInt("schedule_id"),
            rs.getInt("class_id"),
            rs.getString("course_id"),
            rs.getString("course_name"),
            rs.getString("day_of_week"),
            rs.getTime("start_time"),
            rs.getTime("end_time"),
            rs.getString("room"),
            rs.getString("lecturer"),
            rs.getString("semester"),
            rs.getString("academic_year")
        );
    }
}
