
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import Data_Access_Object.RegistrationDAO;
import model.Registration;

public class GradeView extends JFrame {

    private final String studentId;
    private final RegistrationDAO registrationDAO;

    private DefaultTableModel tableModel;
    private JTable table;

    public GradeView(String studentId) {

        this.studentId = studentId;
        this.registrationDAO = new RegistrationDAO();

        setTitle("Bảng điểm - MSSV: " + studentId);
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel(
            "BẢNG ĐIỂM SINH VIÊN", JLabel.CENTER
        );
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel lblStudent = new JLabel(
            "Mã sinh viên: " + studentId, JLabel.CENTER
        );

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.add(lblTitle);
        header.add(lblStudent);

        String[] columns = {
            "Mã học phần",
            "Tên học phần",
            "Điểm"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);

        JButton btnRefresh = new JButton("Cập nhật bảng điểm");
        btnRefresh.addActionListener(e -> loadGrades());

        JPanel bottom = new JPanel();
        bottom.add(btnRefresh);

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadGrades();
    }

    private void loadGrades() {

        tableModel.setRowCount(0);

        List<Registration> list =
            registrationDAO.getGradesByStudent(studentId);

        for (Registration r : list) {

            Object score = r.getScore() == null
                ? "Chưa có điểm"
                : r.getScore();

            tableModel.addRow(new Object[]{
                r.getCourseId(),
                r.getCourseName(),
                score
            });
        }
    }
}
