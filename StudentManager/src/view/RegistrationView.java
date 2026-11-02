
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.regex.Pattern;

import model.Registration;
import Data_Access_Object.RegistrationDAO;

public class RegistrationView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField txtSearch;

    private RegistrationDAO registrationDAO;

    // null: Admin xem toàn bộ đăng ký
    // Có studentId: Student chỉ xem đăng ký của mình
    private String studentId;

    // ==========================================
    // 1. CONSTRUCTOR DÀNH CHO ADMIN
    // ==========================================

    public RegistrationView() {
        this(null);
    }

    // ==========================================
    // 2. CONSTRUCTOR DÀNH CHO SINH VIÊN
    // ==========================================

    public RegistrationView(String studentId) {

        this.studentId = studentId;

        registrationDAO = new RegistrationDAO();

        setTitle(
            studentId == null
                ? "Quản lý đăng ký học phần"
                : "Lịch sử đăng ký học phần - " + studentId
        );

        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadRegistrations();

        // Tự động tải lại khi quay lại cửa sổ lịch sử
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {
                loadRegistrations();
            }
        });
    }

    // ==========================================
    // 3. KHỞI TẠO GIAO DIỆN
    // ==========================================

    private void initComponents() {

        // Tiêu đề
        JLabel lblTitle = new JLabel(
            studentId == null
                ? "QUẢN LÝ ĐĂNG KÝ HỌC PHẦN"
                : "LỊCH SỬ ĐĂNG KÝ HỌC PHẦN",
            JLabel.CENTER
        );

        lblTitle.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        lblTitle.setBorder(
            BorderFactory.createEmptyBorder(15, 0, 10, 0)
        );

        add(lblTitle, BorderLayout.NORTH);

        // Cấu trúc bảng
        String[] columns = {
            "Mã đăng ký",
            "Mã sinh viên",
            "Tên sinh viên",
            "Mã học phần",
            "Tên học phần",
            "Ngày đăng ký",
            "Mã lớp học phần"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        table.setRowHeight(28);

        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // Ẩn cột mã lớp học phần trên giao diện
        // Nhưng vẫn giữ dữ liệu trong TableModel
        table.removeColumn(
            table.getColumnModel().getColumn(6)
        );

        // ======================================
        // KHU VỰC TÌM KIẾM
        // ======================================

        JPanel searchPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        JLabel lblSearch = new JLabel("Tìm kiếm:");

        txtSearch = new JTextField(25);

        JButton btnSearch = new JButton("Tìm kiếm");
        JButton btnRefresh = new JButton("Làm mới");

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);

        // ======================================
        // KHU VỰC NÚT CHỨC NĂNG
        // ======================================

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        JButton btnDelete = new JButton(
            studentId == null
                ? "Xóa đăng ký"
                : "Hủy đăng ký"
        );

        JButton btnRefreshList = new JButton("Tải danh sách");

        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefreshList);

        JPanel bottomPanel = new JPanel(
            new BorderLayout()
        );

        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 10, 10)
        );

        bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // ======================================
        // SỰ KIỆN TÌM KIẾM
        // ======================================

        btnSearch.addActionListener(
            e -> searchRegistration()
        );

        txtSearch.addActionListener(
            e -> searchRegistration()
        );

        // ======================================
        // SỰ KIỆN LÀM MỚI
        // ======================================

        btnRefresh.addActionListener(e -> {

            txtSearch.setText("");
            sorter.setRowFilter(null);

            loadRegistrations();
        });

        btnRefreshList.addActionListener(e -> {
            loadRegistrations();
        });

        // ======================================
        // SỰ KIỆN XÓA / HỦY ĐĂNG KÝ
        // ======================================

        btnDelete.addActionListener(
            e -> deleteRegistration()
        );
    }

    // ==========================================
    // 4. TẢI DANH SÁCH ĐĂNG KÝ TỪ MYSQL
    // ==========================================

    private void loadRegistrations() {

        tableModel.setRowCount(0);

        try {

            List<Registration> registrations;

            if (studentId == null) {

                // Admin: lấy toàn bộ đăng ký
                registrations =
                    registrationDAO.getAllRegistrations();

            } else {

                // Student: chỉ lấy đăng ký của mình
                registrations =
                    registrationDAO.getRegistrationsByStudent(
                        studentId
                    );
            }

            for (Registration r : registrations) {

                Object[] row = {

                    r.getRegistrationId(),

                    r.getStudentId(),

                    r.getFullName(),

                    r.getCourseId(),

                    r.getCourseName(),

                    r.getRegistrationDate(),

                    r.getClassId()
                };

                tableModel.addRow(row);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Không thể tải danh sách đăng ký!\n"
                    + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // 5. TÌM KIẾM ĐĂNG KÝ
    // ==========================================

    private void searchRegistration() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {

            sorter.setRowFilter(null);
            return;
        }

        try {

            sorter.setRowFilter(
                RowFilter.regexFilter(
                    "(?i)" + Pattern.quote(keyword)
                )
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Không thể tìm kiếm dữ liệu!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ==========================================
    // 6. HỦY / XÓA ĐĂNG KÝ
    // ==========================================

    private void deleteRegistration() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Vui lòng chọn lượt đăng ký cần hủy!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Chuyển chỉ số dòng về TableModel
        int modelRow = table.convertRowIndexToModel(
            selectedRow
        );

        int registrationID;
        int classId;

        try {

            registrationID = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
            );

            // Lấy mã lớp học phần từ cột đang ẩn
            classId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 6).toString()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Thông tin đăng ký không hợp lệ!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn "
                + (studentId == null ? "xóa" : "hủy")
                + " lượt đăng ký này?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean success;

            if (studentId == null) {

                // Admin xóa theo mã đăng ký
                success = registrationDAO.deleteRegistration(
                    registrationID
                );

            } else {

                // Student chỉ hủy đăng ký của chính mình
                success = registrationDAO.unregisterClass(
                    studentId,
                    classId
                );
            }

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Thao tác thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );

                // Tải lại danh sách sau khi xóa / hủy
                loadRegistrations();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Thao tác thất bại!\n"
                        + "Vui lòng kiểm tra lại dữ liệu.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Lỗi xử lý đăng ký: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
