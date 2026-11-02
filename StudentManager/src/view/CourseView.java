
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

import model.Course;
import Data_Access_Object.CourseDAO;

public class CourseView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private CourseDAO courseDAO;

    private JTextField txtCourseID;
    private JTextField txtCourseName;
    private JTextField txtCredits;
    private JTextField txtDescription;
    private JTextField txtSearch;

    public CourseView() {

        courseDAO = new CourseDAO();

        setTitle("Quản lý môn học");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadCourses();
    }

    // ==========================================
    // 1. KHOI TAO GIAO DIEN
    // ==========================================
    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel(
                "QUẢN LÝ MÔN HỌC",
                JLabel.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        );

        // FORM NHAP LIEU
        JPanel inputPanel = new JPanel(
                new GridLayout(2, 4, 8, 8)
        );

        inputPanel.setBorder(
                BorderFactory.createTitledBorder("Thông tin môn học")
        );

        txtCourseID = new JTextField();
        txtCourseName = new JTextField();
        txtCredits = new JTextField();
        txtDescription = new JTextField();

        inputPanel.add(new JLabel("Mã môn học:"));
        inputPanel.add(txtCourseID);

        inputPanel.add(new JLabel("Tên môn học:"));
        inputPanel.add(txtCourseName);

        inputPanel.add(new JLabel("Số tín chỉ:"));
        inputPanel.add(txtCredits);

        inputPanel.add(new JLabel("Mô tả:"));
        inputPanel.add(txtDescription);

        // BANG MON HOC
        String[] columns = {
                "Mã môn học",
                "Tên môn học",
                "Số tín chỉ",
                "Mô tả"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setRowHeight(28);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        // CHON MON HOC TREN BANG
        table.getSelectionModel().addListSelectionListener(e -> {

            if (e.getValueIsAdjusting()) {
                return;
            }

            int row = table.getSelectedRow();

            if (row >= 0) {

                txtCourseID.setText(
                        String.valueOf(tableModel.getValueAt(row, 0))
                );

                txtCourseName.setText(
                        String.valueOf(tableModel.getValueAt(row, 1))
                );

                txtCredits.setText(
                        String.valueOf(tableModel.getValueAt(row, 2))
                );

                txtDescription.setText(
                        String.valueOf(tableModel.getValueAt(row, 3))
                );
            }
        });

        // CAC NUT CHUC NANG
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnSearch = new JButton("Tìm kiếm");

        txtSearch = new JTextField(15);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(txtSearch);
        buttonPanel.add(btnSearch);

        // KHU VUC PHIA TREN
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));

        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // SU KIEN NUT
        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());

        btnRefresh.addActionListener(e -> {
            clearFields();
            loadCourses();
        });

        btnSearch.addActionListener(e -> searchCourses());
    }

    // ==========================================
    // 2. LAY DU LIEU TU FORM
    // ==========================================
    private Course getCourseFromForm() {

        String courseId = txtCourseID.getText().trim();
        String courseName = txtCourseName.getText().trim();
        String creditsText = txtCredits.getText().trim();
        String description = txtDescription.getText().trim();

        if (courseId.isEmpty()
                || courseName.isEmpty()
                || creditsText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập mã môn học, tên môn học và số tín chỉ!"
            );

            return null;
        }

        try {

            int credits = Integer.parseInt(creditsText);

            if (credits <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Số tín chỉ phải lớn hơn 0!"
                );

                return null;
            }

            return new Course(
                    courseId,
                    courseName,
                    credits,
                    description
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Số tín chỉ phải là số nguyên!"
            );

            return null;
        }
    }

    // ==========================================
    // 3. THEM MON HOC
    // ==========================================
    private void addCourse() {

        Course course = getCourseFromForm();

        if (course == null) {
            return;
        }

        try {

            if (courseDAO.addCourse(course)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Thêm môn học thành công!"
                );

                loadCourses();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể thêm môn học! Kiểm tra mã môn học."
                );
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ==========================================
    // 4. SUA MON HOC
    // ==========================================
    private void updateCourse() {

        if (txtCourseID.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn môn học cần sửa!"
            );

            return;
        }

        Course course = getCourseFromForm();

        if (course == null) {
            return;
        }

        try {

            if (courseDAO.updateCourse(course)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật môn học thành công!"
                );

                loadCourses();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể cập nhật môn học!"
                );
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ==========================================
    // 5. XOA MON HOC
    // ==========================================
    private void deleteCourse() {

        String courseId = txtCourseID.getText().trim();

        if (courseId.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn môn học cần xóa!"
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa môn học " + courseId + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            if (courseDAO.deleteCourse(courseId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa môn học thành công!"
                );

                loadCourses();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể xóa môn học!"
                );
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ==========================================
    // 6. HIEN THI DANH SACH MON HOC
    // ==========================================
    private void loadCourses() {

        try {

            List<Course> courses = courseDAO.getAllCourses();

            tableModel.setRowCount(0);

            for (Course course : courses) {

                tableModel.addRow(new Object[]{

                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredits(),
                        course.getDescription()
                });
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ==========================================
    // 7. TIM KIEM MON HOC
    // ==========================================
    private void searchCourses() {

        String keyword = txtSearch.getText().trim();

        try {

            List<Course> courses =
                    courseDAO.searchCourses(keyword);

            tableModel.setRowCount(0);

            for (Course course : courses) {

                tableModel.addRow(new Object[]{

                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredits(),
                        course.getDescription()
                });
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ==========================================
    // 8. LAM MOI FORM
    // ==========================================
    private void clearFields() {

        txtCourseID.setText("");
        txtCourseName.setText("");
        txtCredits.setText("");
        txtDescription.setText("");
        txtSearch.setText("");

        table.clearSelection();
    }

    // ==========================================
    // 9. HIEN THI LOI
    // ==========================================
    private void showError(Exception ex) {

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Có lỗi xảy ra: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
