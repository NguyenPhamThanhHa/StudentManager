
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

import model.CourseClass;
import Data_Access_Object.CourseDAO;
import Data_Access_Object.RegistrationDAO;

public class CourseRegistrationView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    private CourseDAO courseDAO;
    private RegistrationDAO registrationDAO;

    private final String studentID;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public CourseRegistrationView(String studentID) {

        this.studentID = studentID;

        courseDAO = new CourseDAO();
        registrationDAO = new RegistrationDAO();

        setTitle("Đăng ký học phần - MSSV: " + studentID);
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadCourseClasses();
    }

    // ==========================================
    // 1. KHỞI TẠO GIAO DIỆN
    // ==========================================

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel(
            "ĐĂNG KÝ HỌC PHẦN",
            JLabel.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel lblStudent = new JLabel(
            "Mã sinh viên: " + studentID
        );

        lblStudent.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel pnlHeader = new JPanel(
            new GridLayout(2, 1, 5, 5)
        );

        pnlHeader.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 5, 10)
        );

        pnlHeader.add(lblTitle);
        pnlHeader.add(lblStudent);

        add(pnlHeader, BorderLayout.NORTH);

        // Bảng danh sách lớp học phần
        String[] columns = {
            "Mã lớp HP",
            "Mã môn học",
            "Tên môn học",
            "Giảng viên",
            "Học kỳ",
            "Năm học",
            "Số tín chỉ",
            "Sĩ số",
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

        table.setAutoResizeMode(
            JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // Khu vực tìm kiếm
        txtSearch = new JTextField(25);

        JButton btnRefresh = new JButton("Làm mới");
        JButton btnRegister = new JButton("Đăng ký học phần");

        JPanel pnlSearch = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        pnlSearch.add(new JLabel("Tìm học phần:"));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnRefresh);

        // Khu vực nút chức năng
        JPanel pnlButtons = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        pnlButtons.add(btnRegister);

        JPanel pnlBottom = new JPanel(new BorderLayout());

        pnlBottom.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 10, 10)
        );

        pnlBottom.add(pnlSearch, BorderLayout.NORTH);
        pnlBottom.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlBottom, BorderLayout.SOUTH);

        // Sắp xếp và tìm kiếm trên bảng
        TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(tableModel);

        table.setRowSorter(sorter);

        txtSearch.getDocument().addDocumentListener(
            new DocumentListener() {

                private void search() {

                    String keyword = txtSearch.getText().trim();

                    if (keyword.isEmpty()) {

                        sorter.setRowFilter(null);

                    } else {

                        sorter.setRowFilter(
                            RowFilter.regexFilter(
                                "(?i)" + Pattern.quote(keyword),
                                0, 1, 2, 3, 4, 5
                            )
                        );
                    }
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    search();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    search();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    search();
                }
            }
        );

        // Sự kiện làm mới
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadCourseClasses();
        });

        // Sự kiện đăng ký
        btnRegister.addActionListener(
            e -> registerCourseClass()
        );
    }

    // ==========================================
    // 2. TẢI DANH SÁCH LỚP HỌC PHẦN ĐANG MỞ
    // ==========================================

    private void loadCourseClasses() {

        tableModel.setRowCount(0);

        try {

            List<CourseClass> classes =
                courseDAO.getOpenCourseClasses();

            for (CourseClass courseClass : classes) {

                tableModel.addRow(new Object[]{

                    courseClass.getClassId(),

                    courseClass.getCourseId(),

                    courseClass.getCourseName(),

                    courseClass.getLecturer(),

                    courseClass.getSemester(),

                    courseClass.getAcademicYear(),

                    courseClass.getCredits(),

                    courseClass.getEnrolled()
                        + " / "
                        + courseClass.getMaxCapacity(),

                    courseClass.getStatus()
                });
            }

            if (classes.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Hiện tại không có lớp học phần nào đang mở!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Không thể tải danh sách lớp học phần: "
                    + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // 3. ĐĂNG KÝ LỚP HỌC PHẦN
    // ==========================================

    private void registerCourseClass() {

        int selectedRow = table.getSelectedRow();

        // Kiểm tra đã chọn lớp học phần chưa
        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Vui lòng chọn lớp học phần muốn đăng ký!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Chuyển dòng được chọn về dòng trong TableModel
        int modelRow = table.convertRowIndexToModel(selectedRow);

        // Lấy thông tin lớp học phần
        int classId = Integer.parseInt(
            tableModel.getValueAt(modelRow, 0).toString()
        );

        String courseId =
            tableModel.getValueAt(modelRow, 1).toString();

        String courseName =
            tableModel.getValueAt(modelRow, 2).toString();

        String lecturer =
            tableModel.getValueAt(modelRow, 3).toString();

        String semester =
            tableModel.getValueAt(modelRow, 4).toString();

        String academicYear =
            tableModel.getValueAt(modelRow, 5).toString();

        // Nội dung xác nhận
        String message =
            "Bạn có chắc chắn muốn đăng ký lớp học phần này?\n\n"
            + "Mã lớp HP: " + classId + "\n"
            + "Mã môn học: " + courseId + "\n"
            + "Tên môn học: " + courseName + "\n"
            + "Giảng viên: " + lecturer + "\n"
            + "Học kỳ: " + semester + "\n"
            + "Năm học: " + academicYear;

        int confirm = JOptionPane.showConfirmDialog(
            this,
            message,
            "Xác nhận đăng ký",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Thực hiện đăng ký
        try {

            boolean success = registrationDAO.registerClass(
                studentID,
                classId
            );

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Đăng ký học phần thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );

                // Cập nhật lại danh sách và sĩ số
                loadCourseClasses();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Đăng ký không thành công!\n"
                    + "Có thể lớp đã đầy, đã đóng đăng ký "
                    + "hoặc bạn đã đăng ký lớp học phần này.",
                    "Lỗi đăng ký",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Lỗi đăng ký học phần: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}