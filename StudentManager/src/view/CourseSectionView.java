package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.util.List;

import model.Course;
import model.CourseClass;
import Data_Access_Object.CourseDAO;

public class CourseSectionView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private CourseDAO courseDAO;

    private JComboBox<Course> cboCourse;
    private JComboBox<String> cboStatus;

    private JTextField txtLecturer;
    private JTextField txtSemester;
    private JTextField txtAcademicYear;
    private JTextField txtCapacity;
    private JTextField txtSearch;

    public CourseSectionView() {

        courseDAO = new CourseDAO();

        setTitle("Quản lý mở lớp theo học kỳ");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadCourses();
        loadCourseClasses();
    }

    // ================= KHỞI TẠO GIAO DIỆN =================

    private void initComponents() {

        JLabel lblTitle = new JLabel(
                "QUẢN LÝ MỞ LỚP THEO HỌC KỲ",
                JLabel.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(15, 0, 15, 0)
        );

        add(lblTitle, BorderLayout.NORTH);

        // BẢNG DỮ LIỆU

        String[] columns = {
            "Mã lớp",
            "Mã học phần",
            "Tên học phần",
            "Tín chỉ",
            "Giảng viên",
            "Học kỳ",
            "Năm học",
            "Sĩ số tối đa",
            "Đã đăng ký",
            "Trạng thái"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // FORM NHẬP LIỆU

        JPanel formPanel = new JPanel(
                new GridLayout(3, 4, 10, 10)
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Thông tin mở lớp học phần"
                )
        );

        cboCourse = new JComboBox<>();

        txtLecturer = new JTextField();
        txtSemester = new JTextField();
        txtAcademicYear = new JTextField();
        txtCapacity = new JTextField();

        cboStatus = new JComboBox<>(
                new String[]{"OPEN", "CLOSED"}
        );

        formPanel.add(new JLabel("Học phần:"));
        formPanel.add(cboCourse);

        formPanel.add(new JLabel("Giảng viên:"));
        formPanel.add(txtLecturer);

        formPanel.add(new JLabel("Học kỳ:"));
        formPanel.add(txtSemester);

        formPanel.add(new JLabel("Năm học:"));
        formPanel.add(txtAcademicYear);

        formPanel.add(new JLabel("Sĩ số tối đa:"));
        formPanel.add(txtCapacity);

        formPanel.add(new JLabel("Trạng thái:"));
        formPanel.add(cboStatus);

        // CÁC NÚT CHỨC NĂNG

        JButton btnAdd = new JButton("Thêm lớp");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa lớp");
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnSearch = new JButton("Tìm kiếm");

        txtSearch = new JTextField(15);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 8)
        );

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(txtSearch);
        buttonPanel.add(btnSearch);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // SỰ KIỆN

        btnAdd.addActionListener(e -> addCourseClass());

        btnUpdate.addActionListener(e -> updateCourseClass());

        btnDelete.addActionListener(e -> deleteCourseClass());

        btnRefresh.addActionListener(e -> clearForm());

        btnSearch.addActionListener(e -> searchCourseClasses());

        txtSearch.addActionListener(e -> searchCourseClasses());

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                showSelectedCourseClass();
            }
        });
    }

    // ================= TẢI DANH SÁCH HỌC PHẦN =================

    private void loadCourses() {

        try {

            cboCourse.removeAllItems();

            List<Course> courses = courseDAO.getAllCourses();

            for (Course course : courses) {
                cboCourse.addItem(course);
            }

            cboCourse.setRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(
                        JList<?> list,
                        Object value,
                        int index,
                        boolean isSelected,
                        boolean cellHasFocus) {

                    super.getListCellRendererComponent(
                            list, value, index,
                            isSelected, cellHasFocus
                    );

                    if (value instanceof Course) {

                        Course c = (Course) value;

                        setText(
                                c.getCourseId() + " - "
                                + c.getCourseName()
                        );
                    }

                    return this;
                }
            });

        } catch (Exception e) {
            showError(e);
        }
    }

    // ================= HIỂN THỊ DANH SÁCH LỚP =================

    private void loadCourseClasses() {

        try {

            tableModel.setRowCount(0);

            List<CourseClass> classes =
                    courseDAO.getAllCourseClasses();

            for (CourseClass c : classes) {

                tableModel.addRow(new Object[]{
                    c.getClassId(),
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getCredits(),
                    c.getLecturer(),
                    c.getSemester(),
                    c.getAcademicYear(),
                    c.getMaxCapacity(),
                    c.getEnrolled(),
                    c.getStatus()
                });
            }

        } catch (Exception e) {
            showError(e);
        }
    }

    // ================= LẤY DỮ LIỆU TỪ FORM =================

    private CourseClass getCourseClassFromForm(int classId) {

        Course course = (Course) cboCourse.getSelectedItem();

        if (course == null) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn học phần!"
            );
        }

        String lecturer = txtLecturer.getText().trim();
        String semester = txtSemester.getText().trim();
        String academicYear = txtAcademicYear.getText().trim();
        String capacityText = txtCapacity.getText().trim();

        if (lecturer.isEmpty()
                || semester.isEmpty()
                || academicYear.isEmpty()
                || capacityText.isEmpty()) {

            throw new IllegalArgumentException(
                    "Vui lòng nhập đầy đủ thông tin!"
            );
        }

        int capacity;

        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Sĩ số tối đa phải là số nguyên!"
            );
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Sĩ số tối đa phải lớn hơn 0!"
            );
        }

        int enrolled = 0;

        if (classId > 0) {

            int row = table.getSelectedRow();

            if (row >= 0) {

                int modelRow = table.convertRowIndexToModel(row);

                enrolled = Integer.parseInt(
                        tableModel.getValueAt(modelRow, 8).toString()
                );
            }
        }

        if (capacity < enrolled) {
            throw new IllegalArgumentException(
                    "Sĩ số tối đa không được nhỏ hơn số sinh viên đã đăng ký!"
            );
        }

        String status = cboStatus.getSelectedItem().toString();

        if ("CLOSED".equals(status) && enrolled > 0) {
            // Cho phép đóng lớp đã có sinh viên đăng ký.
        }

        return new CourseClass(
                classId,
                course.getCourseId(),
                course.getCourseName(),
                course.getCredits(),
                lecturer,
                semester,
                academicYear,
                capacity,
                enrolled,
                status
        );
    }

    // ================= THÊM LỚP HỌC PHẦN =================

    private void addCourseClass() {

        try {

            CourseClass c = getCourseClassFromForm(0);

            if (courseDAO.addCourseClass(c)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Thêm lớp học phần thành công!"
                );

                clearForm();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể thêm lớp học phần!"
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

    // ================= CẬP NHẬT LỚP =================

    private void updateCourseClass() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn lớp cần cập nhật!"
            );
            return;
        }

        try {

            int modelRow = table.convertRowIndexToModel(row);

            int classId = Integer.parseInt(
                    tableModel.getValueAt(modelRow, 0).toString()
            );

            CourseClass c = getCourseClassFromForm(classId);

            if (courseDAO.updateCourseClass(c)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật lớp học phần thành công!"
                );

                clearForm();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể cập nhật lớp học phần!"
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

    // ================= XÓA LỚP =================

    private void deleteCourseClass() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn lớp cần xóa!"
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa lớp học phần này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int modelRow = table.convertRowIndexToModel(row);

            int classId = Integer.parseInt(
                    tableModel.getValueAt(modelRow, 0).toString()
            );

            if (courseDAO.deleteCourseClass(classId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa lớp học phần thành công!"
                );

                clearForm();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể xóa lớp học phần!"
                );
            }

        } catch (Exception e) {
            showError(e);
        }
    }

    // ================= CHỌN LỚP TRÊN BẢNG =================

    private void showSelectedCourseClass() {

        int row = table.getSelectedRow();

        if (row == -1) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        String courseId = tableModel.getValueAt(
                modelRow, 1
        ).toString();

        for (int i = 0; i < cboCourse.getItemCount(); i++) {

            Course c = cboCourse.getItemAt(i);

            if (c.getCourseId().equals(courseId)) {
                cboCourse.setSelectedIndex(i);
                break;
            }
        }

        txtLecturer.setText(
                tableModel.getValueAt(modelRow, 4).toString()
        );

        txtSemester.setText(
                tableModel.getValueAt(modelRow, 5).toString()
        );

        txtAcademicYear.setText(
                tableModel.getValueAt(modelRow, 6).toString()
        );

        txtCapacity.setText(
                tableModel.getValueAt(modelRow, 7).toString()
        );

        cboStatus.setSelectedItem(
                tableModel.getValueAt(modelRow, 9).toString()
        );
    }

    // ================= TÌM KIẾM =================

    private void searchCourseClasses() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {

            sorter.setRowFilter(
                    RowFilter.regexFilter(
                            "(?i)" + java.util.regex.Pattern.quote(keyword)
                    )
            );
        }
    }

    // ================= LÀM MỚI =================

    private void clearForm() {

        table.clearSelection();

        cboCourse.setSelectedIndex(-1);

        txtLecturer.setText("");
        txtSemester.setText("");
        txtAcademicYear.setText("");
        txtCapacity.setText("");
        txtSearch.setText("");

        cboStatus.setSelectedItem("OPEN");

        sorter.setRowFilter(null);

        loadCourseClasses();
    }

    // ================= HIỂN THỊ LỖI =================

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