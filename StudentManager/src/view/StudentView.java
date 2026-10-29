
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
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

    public StudentView() {

        studentDAO = new StudentDAO();

        setTitle("Hệ thống Quản lý Sinh viên");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Bảng sinh viên
        String[] columnNames = {
            "Mã SV", "Họ tên", "Email", "Ngày sinh",
            "Giới tính", "SĐT", "Địa chỉ", "Ngành", "Lớp", "GPA"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 2. Form nhập liệu
        JPanel panelBottom = new JPanel(new GridLayout(2, 1));
        JPanel panelFields = new JPanel(new GridLayout(5, 4, 5, 5));

        panelFields.setBorder(
            BorderFactory.createTitledBorder("Thông tin sinh viên")
        );

        panelFields.add(new JLabel("Mã SV:"));
        txtStudentID = new JTextField();
        panelFields.add(txtStudentID);

        panelFields.add(new JLabel("Họ tên:"));
        txtFullName = new JTextField();
        panelFields.add(txtFullName);

        panelFields.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelFields.add(txtEmail);

        panelFields.add(new JLabel("Ngày sinh (YYYY-MM-DD):"));
        txtDob = new JTextField();
        panelFields.add(txtDob);

        panelFields.add(new JLabel("Giới tính:"));
        txtGender = new JTextField();
        panelFields.add(txtGender);

        panelFields.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        panelFields.add(txtPhone);

        panelFields.add(new JLabel("Địa chỉ:"));
        txtAddress = new JTextField();
        panelFields.add(txtAddress);

        panelFields.add(new JLabel("Ngành học:"));
        txtMajor = new JTextField();
        panelFields.add(txtMajor);

        panelFields.add(new JLabel("Lớp:"));
        txtClassName = new JTextField();
        panelFields.add(txtClassName);

        panelFields.add(new JLabel("GPA:"));
        txtGpa = new JTextField();
        panelFields.add(txtGpa);

        panelBottom.add(panelFields);

        // 3. Các nút chức năng
        JPanel panelButtons = new JPanel();

        JButton btnAdd = new JButton("Thêm sinh viên");
        JButton btnDelete = new JButton("Xóa sinh viên");
        JButton btnRefresh = new JButton("Làm mới");

        panelButtons.add(btnAdd);
        panelButtons.add(btnDelete);
        panelButtons.add(btnRefresh);

        panelBottom.add(panelButtons);
        add(panelBottom, BorderLayout.SOUTH);

        // 4. Thêm sinh viên
        btnAdd.addActionListener(e -> {
            try {
                String studentID = txtStudentID.getText().trim();
                String fullName = txtFullName.getText().trim();
                String email = txtEmail.getText().trim();
                String dobText = txtDob.getText().trim();

                LocalDate dob = dobText.isEmpty()
                    ? null : LocalDate.parse(dobText);

                String gender = txtGender.getText().trim();
                String phone = txtPhone.getText().trim();
                String address = txtAddress.getText().trim();
                String major = txtMajor.getText().trim();
                String className = txtClassName.getText().trim();
                String gpaText = txtGpa.getText().trim();

                if (studentID.isEmpty()
                        || fullName.isEmpty()
                        || gpaText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng nhập mã sinh viên, họ tên và GPA!"
                    );
                    return;
                }

                double gpa = Double.parseDouble(gpaText);

                if (gpa < 0 || gpa > 4) {
                    JOptionPane.showMessageDialog(
                        this, "GPA phải nằm trong khoảng từ 0 đến 4!"
                    );
                    return;
                }

                Student student = new Student(
                    studentID, fullName, email, dob, gender,
                    phone, address, major, className, gpa
                );

                if (studentDAO.addStudent(student)) {
                    JOptionPane.showMessageDialog(
                        this, "Thêm sinh viên thành công!"
                    );

                    clearFields();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Thêm thất bại! Kiểm tra mã sinh viên hoặc CSDL.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    this, "GPA phải là số hợp lệ!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this, "Dữ liệu không hợp lệ: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // 5. Xóa sinh viên
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    this, "Vui lòng chọn sinh viên cần xóa!"
                );
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRow);
            String studentID = tableModel.getValueAt(modelRow, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa sinh viên " + studentID + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (studentDAO.deleteStudent(studentID)) {
                    JOptionPane.showMessageDialog(
                        this, "Xóa sinh viên thành công!"
                    );

                    clearFields();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(
                        this, "Xóa sinh viên thất bại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        // 6. LÀM MỚI: XÓA TRẮNG FORM VÀ BẢNG
        btnRefresh.addActionListener(e -> {
            clearFields();
            tableModel.setRowCount(0);
        });

        // 7. Chọn sinh viên trên bảng để hiện thông tin
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    int modelRow =
                        table.convertRowIndexToModel(selectedRow);

                    txtStudentID.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 0))
                    );
                    txtFullName.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 1))
                    );
                    txtEmail.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 2))
                    );

                    Object dob = tableModel.getValueAt(modelRow, 3);
                    txtDob.setText(dob == null ? "" : dob.toString());

                    txtGender.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 4))
                    );
                    txtPhone.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 5))
                    );
                    txtAddress.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 6))
                    );
                    txtMajor.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 7))
                    );
                    txtClassName.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 8))
                    );
                    txtGpa.setText(
                        String.valueOf(tableModel.getValueAt(modelRow, 9))
                    );
                }
            }
        });

        // 8. Tải dữ liệu lúc mở ứng dụng
        loadDataToTable();
    }

    // Xóa trắng các ô nhập liệu
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

    // Tải danh sách sinh viên từ MySQL
    private void loadDataToTable() {
        tableModel.setRowCount(0);

        try {
            List<Student> list = studentDAO.getAllStudents();

            for (Student s : list) {
                Object[] row = {
                    s.getStudentID(),
                    s.getFullName(),
                    s.getEmail(),
                    s.getDateOfBirth(),
                    s.getGender(),
                    s.getPhone(),
                    s.getAddress(),
                    s.getMajor(),
                    s.getClassName(),
                    s.getGpa()
                };

                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Không thể tải danh sách sinh viên!\n" + ex.getMessage(),
                "Lỗi CSDL",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Chạy chương trình
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentView().setVisible(true);
        });
    }
}
