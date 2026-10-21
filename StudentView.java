package StudentManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class StudentView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;

    // Các ô nhập liệu (Text Fields)
    private JTextField txtStudentID, txtFullName, txtEmail, txtGender, txtPhone, txtAddress, txtMajor, txtClassName, txtGpa, txtDob;

    /**
     * 
     */
    public StudentView() {
        studentDAO = new StudentDAO();
        setTitle("Hệ thống Quản lý Sinh viên");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. PHẦN BẢNG HIỂN THỊ (JTable) ---
        String[] columnNames = {"Mã SV", "Họ tên", "Email", "Ngày sinh", "Giới tính", "SĐT", "Địa chỉ", "Ngành", "Lớp", "GPA"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- 2. PHẦN NHẬP LIỆU & NÚT BẤM (Form & Buttons) ---
        JPanel panelBottom = new JPanel(new GridLayout(2, 1));
        
        // Panel chứa các ô nhập thông tin
        JPanel panelFields = new JPanel(new GridLayout(5, 4, 5, 5));
        panelFields.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

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

        // Panel chứa các nút chức năng (Thêm, Xóa, Làm mới)
        JPanel panelButtons = new JPanel();
        JButton btnAdd = new JButton("Thêm Sinh Viên");
        JButton btnDelete = new JButton("Xóa Sinh Viên");
        JButton btnRefresh = new JButton("Làm mới bảng");

        panelButtons.add(btnAdd);
        panelButtons.add(btnDelete);
        panelButtons.add(btnRefresh);

        panelBottom.add(panelButtons);
        add(panelBottom, BorderLayout.SOUTH);

        // --- 3. XỬ LÝ SỰ KIỆN (Events) ---
        // Nút Tải lại danh sách
        btnRefresh.addActionListener(e -> loadDataToTable());

        // Nút Thêm sinh viên
        btnAdd.addActionListener(e -> {
            try {
                String studentID = txtStudentID.getText();
                String fullName = txtFullName.getText();
                String email = txtEmail.getText();
                LocalDate dob = txtDob.getText().isEmpty() ? null : LocalDate.parse(txtDob.getText());
                String gender = txtGender.getText();
                String phone = txtPhone.getText();
                String address = txtAddress.getText();
                String major = txtMajor.getText();
                String className = txtClassName.getText();
                double gpa = Double.parseDouble(txtGpa.getText());

                Student student = new Student(studentID, fullName, email, dob, gender, phone, address, major, className, gpa);
                if (studentDAO.addStudent(student)) {
                    JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại (Trùng mã SV hoặc lỗi CSDL).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng dữ liệu!\nLỗi: " + ex.getMessage(), "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Xóa sinh viên dựa vào dòng đang chọn trên JTable
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentID = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sinh viên: " + studentID + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (studentDAO.deleteStudent(studentID)) {
                        JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên trên bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Tải dữ liệu ban đầu khi mở ứng dụng
        loadDataToTable();
    }

    // Hàm load danh sách sinh viên từ Database đổ vào JTable
    private void loadDataToTable() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trên bảng
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentView().setVisible(true);
        });
    }
}