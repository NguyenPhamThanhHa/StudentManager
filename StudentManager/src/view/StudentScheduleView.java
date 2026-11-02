
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

import Data_Access_Object.ScheduleDAO;
import model.Schedule;

public class StudentScheduleView extends JFrame {

    private ScheduleDAO scheduleDAO;

    private DefaultTableModel tableModel;
    private JTable table;

    private String studentId;

    public StudentScheduleView(String studentId) {

        this.studentId = studentId;
        this.scheduleDAO = new ScheduleDAO();

        // Thiết lập cửa sổ
        setTitle("Thời khóa biểu cá nhân");
        setSize(1050, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // TIÊU ĐỀ
        JLabel lblTitle = new JLabel(
                "THỜI KHÓA BIỂU CỦA SINH VIÊN",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(15, 0, 10, 0)
        );

        add(lblTitle, BorderLayout.NORTH);

        // BẢNG THỜI KHÓA BIỂU
        String[] columns = {
            "Mã HP",
            "Tên học phần",
            "Thứ",
            "Giờ bắt đầu",
            "Giờ kết thúc",
            "Phòng học",
            "Giảng viên",
            "Học kỳ",
            "Năm học"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // NÚT CẬP NHẬT
        JButton btnRefresh = new JButton(
                "Cập nhật thời khóa biểu"
        );

        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        bottomPanel.add(btnRefresh);

        add(bottomPanel, BorderLayout.SOUTH);

        // SỰ KIỆN CẬP NHẬT
        btnRefresh.addActionListener(e -> loadSchedule());

        // TẢI DỮ LIỆU BAN ĐẦU
        loadSchedule();
    }

    // TẢI THỜI KHÓA BIỂU CỦA SINH VIÊN
    private void loadSchedule() {

        tableModel.setRowCount(0);

        try {

            List<Schedule> schedules =
                    scheduleDAO.getStudentSchedules(studentId);

            if (schedules == null || schedules.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Sinh viên chưa có thời khóa biểu!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            for (Schedule s : schedules) {

                tableModel.addRow(new Object[]{
                    s.getCourseId(),
                    s.getCourseName(),
                    s.getDayName(),
                    s.getStartTime(),
                    s.getEndTime(),
                    s.getRoom(),
                    s.getLecturer(),
                    s.getSemester(),
                    s.getAcademicYear()
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thời khóa biểu!\n" + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}
