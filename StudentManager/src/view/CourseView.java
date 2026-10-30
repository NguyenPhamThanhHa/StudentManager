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
 private String lecturer;
private String semester;

    private JTextField txtCourseID;
    private JTextField txtCourseName;
    private JTextField txtCredits;
    private JTextField txtLecturer;
    private JTextField txtSemester;
    private JTextField txtMaxStudents;
    private JTextField txtSearch;

public String getLecturer() {
    return lecturer;
}

public void setLecturer(String lecturer) {
    this.lecturer = lecturer;
}

public String getSemester() {
    return semester;
}

public void setSemester(String semester) {
    this.semester = semester;
}

    public CourseView() {
    courseDAO = new CourseDAO();

    setTitle("Quản lý học phần");
    setSize(1000, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    initComponents();
    loadCourses();
}

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel(
                "QUẢN LÝ HỌC PHẦN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Khu vực nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 8, 8));
        inputPanel.setBorder(
                BorderFactory.createTitledBorder("Thông tin học phần"));

        txtCourseID = new JTextField();
        txtCourseName = new JTextField();
        txtCredits = new JTextField();
        txtLecturer = new JTextField();
        txtSemester = new JTextField();
        txtMaxStudents = new JTextField();

        inputPanel.add(new JLabel("Mã học phần:"));
        inputPanel.add(txtCourseID);
        inputPanel.add(new JLabel("Tên học phần:"));
        inputPanel.add(txtCourseName);

        inputPanel.add(new JLabel("Số tín chỉ:"));
        inputPanel.add(txtCredits);
        inputPanel.add(new JLabel("Giảng viên:"));
        inputPanel.add(txtLecturer);

        inputPanel.add(new JLabel("Học kỳ:"));
        inputPanel.add(txtSemester);
        inputPanel.add(new JLabel("Sĩ số tối đa:"));
        inputPanel.add(txtMaxStudents);

        // Bảng dữ liệu
        String[] columns = {
            "Mã học phần", "Tên học phần", "Số tín chỉ",
            "Giảng viên", "Học kỳ", "Sĩ số tối đa"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        // Khi chọn dòng, đưa dữ liệu lên các ô nhập
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();

                if (row >= 0) {
                    txtCourseID.setText(
                            tableModel.getValueAt(row, 0).toString());
                    txtCourseName.setText(
                            tableModel.getValueAt(row, 1).toString());
                    txtCredits.setText(
                            tableModel.getValueAt(row, 2).toString());
                    txtLecturer.setText(
                            tableModel.getValueAt(row, 3).toString());
                    txtSemester.setText(
                            tableModel.getValueAt(row, 4).toString());
                    txtMaxStudents.setText(
                            tableModel.getValueAt(row, 5).toString());
                }
            }
        });

        // Các nút chức năng
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

        // Khu vực phía trên
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Sự kiện nút
        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnRefresh.addActionListener(e -> {
            clearFields();
            loadCourses();
        });
        btnSearch.addActionListener(e -> searchCourses());
    }

    private Course getCourseFromForm() {
        String courseID = txtCourseID.getText().trim();
        String courseName = txtCourseName.getText().trim();
        String lecturer = txtLecturer.getText().trim();
        String semester = txtSemester.getText().trim();

        if (courseID.isEmpty() || courseName.isEmpty()
                || txtCredits.getText().trim().isEmpty()
                || lecturer.isEmpty() || semester.isEmpty()
                || txtMaxStudents.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Vui lòng nhập đầy đủ thông tin!");
            return null;
        }

        try {
            int credits = Integer.parseInt(
                    txtCredits.getText().trim());
            int maxStudents = Integer.parseInt(
                    txtMaxStudents.getText().trim());

            if (credits <= 0 || maxStudents <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Số tín chỉ và sĩ số phải lớn hơn 0!");
                return null;
            }

            return new Course(courseID, courseName, credits,
                    lecturer, semester, maxStudents);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Số tín chỉ và sĩ số phải là số nguyên!");
            return null;
        }
    }

    private void addCourse() {
        Course course = getCourseFromForm();

        if (course == null) {
            return;
        }

        try {
            if (courseDAO.addCourse(course)) {
                JOptionPane.showMessageDialog(
                        this, "Thêm học phần thành công!");
                loadCourses();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                        this, "Không thể thêm học phần!");
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void updateCourse() {
        Course course = getCourseFromForm();

        if (course == null) {
            return;
        }

        try {
            if (courseDAO.updateCourse(course)) {
                JOptionPane.showMessageDialog(
                        this, "Cập nhật học phần thành công!");
                loadCourses();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                        this, "Không tìm thấy học phần cần sửa!");
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void deleteCourse() {
        String courseID = txtCourseID.getText().trim();

        if (courseID.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Vui lòng chọn học phần cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa học phần " + courseID + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (courseDAO.deleteCourse(courseID)) {
                JOptionPane.showMessageDialog(
                        this, "Xóa học phần thành công!");
                loadCourses();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                        this, "Không thể xóa học phần!");
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadCourses() {
        try {
            List<Course> courses = courseDAO.getAllCourses();

            tableModel.setRowCount(0);

            for (Course course : courses) {
                tableModel.addRow(new Object[]{
                    course.getCourseID(),
                    course.getCourseName(),
                    course.getCredits(),
                    course.getLecturer(),
                    course.getSemester(),
                    course.getMaxStudents()
                });
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void searchCourses() {
        String keyword = txtSearch.getText().trim();

        try {
            List<Course> courses =
                    courseDAO.searchCourses(keyword);

            tableModel.setRowCount(0);

            for (Course course : courses) {
                tableModel.addRow(new Object[]{
                    course.getCourseID(),
                    course.getCourseName(),
                    course.getCredits(),
                    course.getLecturer(),
                    course.getSemester(),
                    course.getMaxStudents()
                });
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void clearFields() {
        txtCourseID.setText("");
        txtCourseName.setText("");
        txtCredits.setText("");
        txtLecturer.setText("");
        txtSemester.setText("");
        txtMaxStudents.setText("");
        txtSearch.setText("");

        table.clearSelection();
    }

    private void showError(Exception ex) {
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Có lỗi xảy ra: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
    }
}
