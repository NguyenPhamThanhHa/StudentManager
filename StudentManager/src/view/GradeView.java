
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
            "Điểm giữa kỳ",
            "Điểm cuối kỳ",
            "Tổng kết",
            "Kết quả"
        }
        : new String[]{
            "Mã học phần",
            "Tên học phần",
            "Điểm giữa kỳ",
            "Điểm cuối kỳ",
            "Tổng kết",
            "Kết quả"
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
    r.getMidtermScore() == null ? "Chưa có điểm" : r.getMidtermScore(),
    r.getFinalScore() == null ? "Chưa có điểm" : r.getFinalScore(),
    score,
    r.getResult()
    });
        }
    }

    // ==========================================
    // ADMIN: NHẬP / CẬP NHẬT ĐIỂM


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

    // Lấy điểm giữa kỳ hiện tại
    Object currentMidterm = tableModel.getValueAt(modelRow, 5);

    String initialMidterm = currentMidterm.equals("Chưa có điểm")
            ? ""
            : currentMidterm.toString();

    // Lấy điểm cuối kỳ hiện tại
    Object currentFinal = tableModel.getValueAt(modelRow, 6);

    String initialFinal = currentFinal.equals("Chưa có điểm")
            ? ""
            : currentFinal.toString();

    // Tạo hai ô nhập điểm
    JTextField txtMidterm = new JTextField(initialMidterm, 10);
    JTextField txtFinal = new JTextField(initialFinal, 10);

    JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 12));

    inputPanel.add(new JLabel("Sinh viên:"));
    inputPanel.add(new JLabel(studentName));

    inputPanel.add(new JLabel("Điểm giữa kỳ:"));
    inputPanel.add(txtMidterm);

    inputPanel.add(new JLabel("Điểm cuối kỳ:"));
    inputPanel.add(txtFinal);

    inputPanel.add(new JLabel("Công thức tổng kết:"));
    inputPanel.add(new JLabel("30% giữa kỳ + 70% cuối kỳ"));

    int option = JOptionPane.showConfirmDialog(
            this,
            inputPanel,
            "Nhập / Cập nhật điểm",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (option != JOptionPane.OK_OPTION) {
        return;
    }

    try {

        String midtermInput = txtMidterm.getText()
                .trim().replace(',', '.');

        String finalInput = txtFinal.getText()
                .trim().replace(',', '.');

        // Kiểm tra dữ liệu nhập
        if (midtermInput.isEmpty() || finalInput.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ điểm giữa kỳ và cuối kỳ!"
            );
            return;
        }

        double midtermScore = Double.parseDouble(midtermInput);
        double finalExamScore = Double.parseDouble(finalInput);

        //Kiểm tra điểm từ 0 đến 10
        if (!Double.isFinite(midtermScore)
                || !Double.isFinite(finalExamScore)
                || midtermScore < 0 || midtermScore > 10
                || finalExamScore < 0 || finalExamScore > 10) {

            JOptionPane.showMessageDialog(
                    this,
                    "Điểm phải nằm trong khoảng từ 0 đến 10!"
            );
            return;
        }

        // Hệ thống tự tính điểm tổng kết
        double totalScore = Math.round(
                (midtermScore * 0.3 + finalExamScore * 0.7) * 100.0
        ) / 100.0;

        String result = totalScore >= 5 ? "Đạt" : "Rớt";

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Sinh viên: " + studentName
                + "\nĐiểm giữa kỳ: " + midtermScore
                + "\nĐiểm cuối kỳ: " + finalExamScore
                + "\nĐiểm tổng kết: " + totalScore
                + "\nKết quả: " + result
                + "\n\nXác nhận lưu điểm?",
                "Xác nhận cập nhật",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Gửi hai đầu điểm xuống DAO
        boolean success = registrationDAO.updateGrade(
                registrationId,
                midtermScore,
                finalExamScore
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

    } catch (Exception e) {
        showError(e);
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
    r.getMidtermScore() == null ? "Chưa có điểm" : r.getMidtermScore(),
    r.getFinalScore() == null ? "Chưa có điểm" : r.getFinalScore(),
    score,
    r.getResult()
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
