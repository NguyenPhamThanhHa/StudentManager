package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.sql.Date;
import java.util.List;

import Data_Access_Object.StudentDAO;
import model.Student;

public class StudentView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private StudentDAO studentDAO;

    private JTextField txtStudentID;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtGender;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JTextField txtMajor;
    private JTextField txtClassName;
    private JTextField txtGpa;
    private JTextField txtDob;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public StudentView() {

        studentDAO = new StudentDAO();

        setTitle("Hệ thống quản lý sinh viên");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadDataToTable();
    }

    // ==========================================
    // 1. KHỞI TẠO GIAO DIỆN
    // ==========================================

    private void initComponents() {

        // Tiêu đề
        JLabel lblTitle = new JLabel(
            "QUẢN LÝ SINH VIÊN",
            JLabel.CENTER
        );

        lblTitle.setFont(new java.awt.Font(
            "Arial",
            java.awt.Font.BOLD,
            22
        ));

        lblTitle.setBorder(
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        add(lblTitle, BorderLayout.NORTH);

        // Bảng sinh viên
        String[] columnNames = {
            "Mã SV",
            "Họ tên",
            "Email",
            "Ngày sinh",
            "Giới tính",
            "SĐT",
            "Địa chỉ",
            "Ngành",
            "Lớp",
            "GPA"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.setRowHeight(25);
        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // Form nhập thông tin
        JPanel panelBottom = new JPanel(new BorderLayout(5, 5));

        JPanel panelFields = new JPanel(
            new GridLayout(5, 4, 8, 8)
        );

        panelFields.setBorder(
            BorderFactory.createTitledBorder(
                "Thông tin sinh viên"
            )
        );

        // Mã sinh viên
        panelFields.add(new JLabel("Mã sinh viên:"));
        txtStudentID = new JTextField();
        panelFields.add(txtStudentID);

        // Họ tên
        panelFields.add(new JLabel("Họ tên:"));
        txtFullName = new JTextField();
        panelFields.add(txtFullName);

        // Email
        panelFields.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelFields.add(txtEmail);

        // Ngày sinh
        panelFields.add(new JLabel("Ngày sinh (YYYY-MM-DD):"));
        txtDob = new JTextField();
        panelFields.add(txtDob);

        // Giới tính
        panelFields.add(new JLabel("Giới tính:"));
        txtGender = new JTextField();
        panelFields.add(txtGender);

        // Số điện thoại
        panelFields.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        panelFields.add(txtPhone);

        // Địa chỉ
        panelFields.add(new JLabel("Địa chỉ:"));
        txtAddress = new JTextField();
        panelFields.add(txtAddress);

        // Ngành học
        panelFields.add(new JLabel("Ngành học:"));
        txtMajor = new JTextField();
        panelFields.add(txtMajor);

        // Lớp
        panelFields.add(new JLabel("Lớp:"));
        txtClassName = new JTextField();
        panelFields.add(txtClassName);

        // GPA
        panelFields.add(new JLabel("GPA (0 - 4):"));
        txtGpa = new JTextField();
        panelFields.add(txtGpa);

        panelBottom.add(panelFields, BorderLayout.CENTER);

        // Các nút chức năng
        JPanel panelButtons = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 12, 10)
        );

        JButton btnAdd = new JButton("Thêm sinh viên");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa sinh viên");
        JButton btnRefresh = new JButton("Làm mới");

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnRefresh);

        panelBottom.add(panelButtons, BorderLayout.SOUTH);

        panelBottom.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 10, 10)
        );

        add(panelBottom, BorderLayout.SOUTH);

        // ==========================================
        // 2. SỰ KIỆN CÁC NÚT
        // ==========================================

        btnAdd.addActionListener(e -> addStudent());

        btnUpdate.addActionListener(e -> updateStudent());

        btnDelete.addActionListener(e -> deleteStudent());

        btnRefresh.addActionListener(e -> {
            clearFields();
            loadDataToTable();
        });

        // Chọn sinh viên trên bảng để hiển thị thông tin
        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {

                    int modelRow =
                        table.convertRowIndexToModel(selectedRow);

                    displayStudentInfo(modelRow);
                }
            }
        });
    }

    // ==========================================
    // 3. LẤY DỮ LIỆU TỪ FORM
    // ==========================================

    private Student getStudentFromForm() throws Exception {

        String studentID = txtStudentID.getText().trim();
        String fullName = txtFullName.getText().trim();

        String email = txtEmail.getText().trim();
        String dobText = txtDob.getText().trim();

        String gender = txtGender.getText().trim();
        String phone = txtPhone.getText().trim();

        String address = txtAddress.getText().trim();
        String major = txtMajor.getText().trim();

        String className = txtClassName.getText().trim();
        String gpaText = txtGpa.getText().trim();

        // Kiểm tra thông tin bắt buộc
        if (studentID.isEmpty() || fullName.isEmpty()) {

            throw new Exception(
                "Vui lòng nhập mã sinh viên và họ tên!"
            );
        }

        // Kiểm tra ngày sinh
        Date birthDate = null;

        if (!dobText.isEmpty()) {

            try {
                birthDate = Date.valueOf(dobText);

            } catch (IllegalArgumentException e) {

                throw new Exception(
                    "Ngày sinh phải có định dạng YYYY-MM-DD!"
                );
            }
        }

        // Kiểm tra GPA
        Double gpa = null;

        if (!gpaText.isEmpty()) {

            try {
                gpa = Double.parseDouble(gpaText);

            } catch (NumberFormatException e) {

                throw new Exception("GPA phải là số hợp lệ!");
            }

            if (gpa < 0 || gpa > 4) {

                throw new Exception(
                    "GPA phải nằm trong khoảng từ 0 đến 4!"
                );
            }
        }

        // Thứ tự tham số đúng với Student.java
        return new Student(
            studentID,
            fullName,
            birthDate,
            gender,
            email,
            phone,
            address,
            major,
            className,
            gpa
        );
    }

    // ==========================================
    // 4. THÊM SINH VIÊN
    // ==========================================

    private void addStudent() {

        try {

            Student student = getStudentFromForm();

            boolean success = studentDAO.addStudent(student);

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Thêm sinh viên thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadDataToTable();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Thêm sinh viên thất bại!\n"
                        + "Mã sinh viên có thể đã tồn tại.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ==========================================
    // 5. CẬP NHẬT SINH VIÊN
    // ==========================================

    private void updateStudent() {

        if (table.getSelectedRow() == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Vui lòng chọn sinh viên cần cập nhật!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Student student = getStudentFromForm();

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn cập nhật thông tin sinh viên "
                    + student.getStudentId() + "?",
                "Xác nhận cập nhật",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            boolean success = studentDAO.updateStudent(student);

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật sinh viên thành công!"
                );

                clearFields();
                loadDataToTable();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật sinh viên thất bại!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // 6. XÓA SINH VIÊN
    // ==========================================

    private void deleteStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Vui lòng chọn sinh viên cần xóa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
            table.convertRowIndexToModel(selectedRow);

        String studentID = tableModel.getValueAt(
            modelRow,
            0
        ).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa sinh viên "
                + studentID + "?\n"
                + "Các dữ liệu liên quan có thể bị xóa theo khóa ngoại.",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean success = studentDAO.deleteStudent(studentID);

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Xóa sinh viên thành công!"
                );

                clearFields();
                loadDataToTable();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Xóa sinh viên thất bại!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Lỗi khi xóa sinh viên: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // 7. HIỂN THỊ THÔNG TIN SINH VIÊN
    // ==========================================

    private void displayStudentInfo(int row) {

        txtStudentID.setText(
            String.valueOf(tableModel.getValueAt(row, 0))
        );

        txtFullName.setText(
            String.valueOf(tableModel.getValueAt(row, 1))
        );

        txtEmail.setText(
            getCellText(row, 2)
        );

        txtDob.setText(
            getCellText(row, 3)
        );

        txtGender.setText(
            getCellText(row, 4)
        );

        txtPhone.setText(
            getCellText(row, 5)
        );

        txtAddress.setText(
            getCellText(row, 6)
        );

        txtMajor.setText(
            getCellText(row, 7)
        );

        txtClassName.setText(
            getCellText(row, 8)
        );

        txtGpa.setText(
            getCellText(row, 9)
        );
    }

    // Lấy dữ liệu trong ô, xử lý giá trị null
    private String getCellText(int row, int column) {

        Object value = tableModel.getValueAt(row, column);

        return value == null ? "" : value.toString();
    }

    // ==========================================
    // 8. XÓA TRẮNG FORM
    // ==========================================

    private void clearFields() {

        txtStudentID.setText("");
        txtFullName.setText("");
        txtEmail.setText("");
        txtDob.setText("");
        txtGender.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtMajor.setText("");
        txtClassName.setText("");
        txtGpa.setText("");

        table.clearSelection();
    }

    // ==========================================
    // 9. TẢI DANH SÁCH SINH VIÊN TỪ MYSQL
    // ==========================================

    private void loadDataToTable() {

        tableModel.setRowCount(0);

        try {

            List<Student> list = studentDAO.getAllStudents();

            for (Student s : list) {

                Object[] row = {

                    s.getStudentId(),

                    s.getFullName(),

                    s.getEmail(),

                    s.getBirthDate(),

                    s.getGender(),

                    s.getPhone(),

                    s.getAddress(),

                    s.getMajor(),

                    s.getClassName(),

                    s.getGpa()
                };

                tableModel.addRow(row);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Không thể tải danh sách sinh viên!\n"
                    + e.getMessage(),
                "Lỗi cơ sở dữ liệu",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // 10. CHẠY RIÊNG GIAO DIỆN
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new StudentView().setVisible(true);
        });
    }
}
