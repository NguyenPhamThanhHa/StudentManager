
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

import model.Registration;
import model.CourseClass;

import Data_Access_Object.RegistrationDAO;
import Data_Access_Object.CourseDAO;

public class GradeView extends JFrame {

    private final RegistrationDAO registrationDAO;
    private final CourseDAO courseDAO;

    private final String studentId;
    private final boolean adminMode;

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<CourseClass> cboClass;

    // ==========================================
    // CONSTRUCTOR DÀNH CHO SINH VIÊN
    // ==========================================
    public GradeView(String studentId) {

        this.studentId = studentId;
        this.adminMode = false;

        registrationDAO = new RegistrationDAO();
        courseDAO = new CourseDAO();

        initView();
    }

    // ==========================================
    // CONSTRUCTOR DÀNH CHO ADMIN
    // ==========================================
    public GradeView() {

        this.studentId = null;
        this.adminMode = true;

        registrationDAO = new RegistrationDAO();
        courseDAO = new CourseDAO();

        initView();
        loadClasses();
    }

    // ==========================================
    // KHỞI TẠO GIAO DIỆN
    // ==========================================
    private void initView() {

        setTitle(adminMode
                ? "Quản lý điểm sinh viên"
                : "Bảng điểm sinh viên");

        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel(
                adminMode
                        ? "QUẢN LÝ ĐIỂM SINH VIÊN"
                        : "BẢNG ĐIỂM SINH VIÊN",
                JLabel.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        add(lblTitle, BorderLayout.NORTH);

        // BẢNG ĐIỂM

        String[] columns = adminMode
                ? new String[]{
                    "Mã đăng ký",
                    "Mã sinh viên",
                    "Họ và tên",
                    "Mã học phần",
                    "Tên học phần",
                    "Điểm"
                }
                : new String[]{
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
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // KHU VỰC CHỨC NĂNG

        JPanel bottomPanel = new JPanel(new BorderLayout());

        if (adminMode) {

            JPanel selectPanel = new JPanel(
                    new FlowLayout(FlowLayout.LEFT, 10, 10)
            );

            selectPanel.add(new JLabel("Lớp học phần:"));

            cboClass = new JComboBox<>();
            cboClass.setPreferredSize(new Dimension(400, 30));

            cboClass.setRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(
                        JList<?> list,
                        Object value,
                        int index,
                        boolean isSelected,
                        boolean cellHasFocus) {

                    super.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus
                    );

                    if (value instanceof CourseClass) {

                        CourseClass c = (CourseClass) value;

                        setText(
                                "Lớp " + c.getClassId()
                                + " - " + c.getCourseName()
                                + " - " + c.getSemester()
                                + " - " + c.getAcademicYear()
                        );
                    }

                    return this;
                }
            });

            selectPanel.add(cboClass);

            JButton btnLoad = new JButton("Tải sinh viên");
            selectPanel.add(btnLoad);

            bottomPanel.add(selectPanel, BorderLayout.NORTH);

            JButton btnUpdate = new JButton("Nhập / Cập nhật điểm");
            JButton btnRefresh = new JButton("Làm mới");

            JPanel buttonPanel = new JPanel();

            buttonPanel.add(btnUpdate);
            buttonPanel.add(btnRefresh);

            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

            btnLoad.addActionListener(e -> loadStudentsByClass());

            cboClass.addActionListener(e -> loadStudentsByClass());

            btnUpdate.addActionListener(e -> updateGrade());

            btnRefresh.addActionListener(e -> loadStudentsByClass());

        } else {

            JLabel lblStudent = new JLabel(
                    "Mã sinh viên: " + studentId,
                    JLabel.CENTER
            );

            JButton btnRefresh = new JButton("Cập nhật bảng điểm");

            JPanel studentPanel = new JPanel(new BorderLayout());

            studentPanel.add(lblStudent, BorderLayout.NORTH);
            studentPanel.add(btnRefresh, BorderLayout.SOUTH);

            bottomPanel.add(studentPanel, BorderLayout.CENTER);

            btnRefresh.addActionListener(e -> loadStudentGrades());

            loadStudentGrades();
        }

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ==========================================
    // ADMIN: TẢI DANH SÁCH LỚP HỌC PHẦN
    // ==========================================
    private void loadClasses() {

        try {

            cboClass.removeAllItems();

            List<CourseClass> classes =
                    courseDAO.getAllCourseClasses();

            for (CourseClass c : classes) {
                cboClass.addItem(c);
            }

            if (cboClass.getItemCount() == 0) {
                tableModel.setRowCount(0);

                JOptionPane.showMessageDialog(
                        this,
                        "Chưa có lớp học phần nào!"
                );
            }

        } catch (Exception e) {
            showError(e);
        }
    }

    // ==========================================
    // ADMIN: TẢI SINH VIÊN THEO LỚP
    // ==========================================
    private void loadStudentsByClass() {

        if (!adminMode) {
            return;
        }

        tableModel.setRowCount(0);

        CourseClass selectedClass =
                (CourseClass) cboClass.getSelectedItem();

        if (selectedClass == null) {
            return;
        }

        List<Registration> list =
                registrationDAO.getRegistrationsByClass(
                        selectedClass.getClassId()
                );

        for (Registration r : list) {

            Object score = r.getScore() == null
                    ? "Chưa có điểm"
                    : r.getScore();

            tableModel.addRow(new Object[]{
                r.getRegistrationId(),
                r.getStudentId(),
                r.getFullName(),
                r.getCourseId(),
                r.getCourseName(),
                score
            });
        }
    }

    // ==========================================
    // ADMIN: NHẬP / CẬP NHẬT ĐIỂM
    // ==========================================
    private void updateGrade() {

        if (!adminMode) {
            return;
        }

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn sinh viên cần cập nhật điểm!"
            );

            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);

        int registrationId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        String studentName =
                tableModel.getValueAt(modelRow, 2).toString();

        Object currentScore =
                tableModel.getValueAt(modelRow, 5);

        String initialValue = currentScore.equals("Chưa có điểm")
                ? ""
                : currentScore.toString();

        String input = JOptionPane.showInputDialog(
                this,
                "Nhập điểm cho sinh viên: " + studentName
                + "\nĐiểm hợp lệ từ 0 đến 10:",
                initialValue
        );

        if (input == null) {
            return;
        }

        input = input.trim().replace(',', '.');

        try {

            double score = Double.parseDouble(input);

            if (!Double.isFinite(score)
                    || score < 0 || score > 10) {

                JOptionPane.showMessageDialog(
                        this,
                        "Điểm phải nằm trong khoảng từ 0 đến 10!"
                );

                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Xác nhận cập nhật điểm "
                    + score + " cho sinh viên " + studentName + "?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            boolean success = registrationDAO.updateGrade(
                    registrationId,
                    score
            );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật điểm thành công!"
                );

                loadStudentsByClass();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật điểm thất bại!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập điểm bằng số hợp lệ!"
            );
        }
    }

    // ==========================================
    // SINH VIÊN: XEM BẢNG ĐIỂM
    // ==========================================
    private void loadStudentGrades() {

        if (adminMode) {
            return;
        }

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

    // ==========================================
    // HIỂN THỊ LỖI
    // ==========================================
    private void showError(Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Có lỗi xảy ra: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
