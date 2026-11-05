package view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class MainView extends JFrame {

    private User user;

    // ==========================================
    // 1. CONSTRUCTOR
    // ==========================================

    public MainView(User user) {

        this.user = user;

        setTitle("Hệ thống quản lý sinh viên và đăng ký học phần");
        setSize(950, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    // ==========================================
    // 2. KHỞI TẠO GIAO DIỆN
    // ==========================================

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        // ======================================
        // HEADER
        // ======================================

        JLabel lblTitle = new JLabel(
            "HỆ THỐNG QUẢN LÝ SINH VIÊN",
            JLabel.CENTER
        );

        lblTitle.setFont(
            new Font("Arial", Font.BOLD, 25)
        );

        JLabel lblUsername = new JLabel(
            "Xin chào: " + user.getUsername(),
            JLabel.CENTER
        );

        lblUsername.setFont(
            new Font("Arial", Font.BOLD, 16)
        );

        JLabel lblRole = new JLabel(
            "Vai trò: " + (
                user.isAdmin()
                    ? "Quản trị viên"
                    : "Sinh viên"
            ),
            JLabel.CENTER
        );

        lblRole.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        JPanel headerPanel = new JPanel(
            new GridLayout(3, 1, 5, 5)
        );

        headerPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 10, 15, 10)
        );

        headerPanel.add(lblTitle);
        headerPanel.add(lblUsername);
        headerPanel.add(lblRole);

        add(headerPanel, BorderLayout.NORTH);

        // ======================================
        // MENU
        // ======================================

        JPanel menuPanel = new JPanel(
            new GridLayout(0, 2, 20, 20)
        );

        menuPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 50, 25, 50)
        );

        if (user.isAdmin()) {

            // ==================================
            // CHỨC NĂNG QUẢN TRỊ VIÊN
            // ==================================

            addMenuButton(
                menuPanel,
                "Quản lý sinh viên",
                () -> new StudentView().setVisible(true)
            );

            addMenuButton(
                menuPanel,
                "Quản lý môn học",
                () -> new CourseView().setVisible(true)
            );

            addMenuButton(
                menuPanel,
                "Quản lý học phần",
                () -> new CourseSectionView().setVisible(true)
            );

            addMenuButton(
                menuPanel,
                "Quản lý lịch học",
                () -> new ScheduleView().setVisible(true)
            );

            addMenuButton(
                menuPanel,
                "Quản lý đăng ký học phần",
                () -> new RegistrationView().setVisible(true)
            );

            addMenuButton(
                menuPanel,
                "Quản lý điểm",
                () -> new GradeView().setVisible(true)
            );

        } else {

            // ==================================
            // [SỬA MỚI] THÔNG TIN SINH VIÊN
            // ==================================

            String studentId = user.getStudentId();

            JPanel studentInfoPanel = createStudentInfoPanel(studentId);

            // Đặt thông tin sinh viên ở phía trên menu.
            JPanel studentContentPanel = new JPanel(new BorderLayout(10, 10));

            studentContentPanel.add(
                studentInfoPanel,
                BorderLayout.NORTH
            );

            studentContentPanel.add(
                menuPanel,
                BorderLayout.CENTER
            );

            // ==================================
            // CHỨC NĂNG SINH VIÊN
            // ==================================

            addMenuButton(
                menuPanel,
                "Đăng ký học phần",
                () -> openStudentWindow(
                    studentId,
                    "Đăng ký học phần",
                    () -> new CourseRegistrationView(studentId).setVisible(true)
                )
            );

            addMenuButton(
                menuPanel,
                "Lịch sử đăng ký học phần",
                () -> openStudentWindow(
                    studentId,
                    "Lịch sử đăng ký học phần",
                    () -> new RegistrationView(studentId).setVisible(true)
                )
            );

            addMenuButton(
                menuPanel,
                "Thời khóa biểu",
                () -> openStudentWindow(
                    studentId,
                    "Thời khóa biểu",
                    () -> new StudentScheduleView(studentId).setVisible(true)
                )
            );

            addMenuButton(
                menuPanel,
                "Xem điểm",
                () -> openStudentWindow(
                    studentId,
                    "Xem điểm",
                    () -> new GradeView(studentId).setVisible(true)
                )
            );

            // [SỬA MỚI] Dùng vùng cuộn vì thông tin và menu
            // có thể vượt quá chiều cao cửa sổ.
            JScrollPane scrollPane = new JScrollPane(studentContentPanel);

            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            add(scrollPane, BorderLayout.CENTER);
        }

        if (user.isAdmin()) {
            add(menuPanel, BorderLayout.CENTER);
        }

        // ======================================
        // FOOTER
        // ======================================

        JPanel bottomPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        JButton btnLogout = new JButton("Đăng xuất");

        btnLogout.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        btnLogout.addActionListener(e -> logout());

        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 15, 15, 15)
        );

        bottomPanel.add(btnLogout);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ==========================================
    // [SỬA MỚI] 3. TẠO PANEL THÔNG TIN SINH VIÊN
    // ==========================================

    private JPanel createStudentInfoPanel(String studentId) {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    "THÔNG TIN CÁ NHÂN SINH VIÊN"
                ),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            )
        );

        if (studentId == null || studentId.trim().isEmpty()) {

            panel.add(
                new JLabel("Tài khoản chưa được liên kết với mã sinh viên."),
                BorderLayout.CENTER
            );

            return panel;
        }

        // [SỬA MỚI] Truy vấn thông tin sinh viên và GPA.
        String sql = """
            SELECT
                student_id,
                full_name,
                birth_date,
                gender,
                email,
                phone,
                address,
                major,
                class_name,
                gpa
            FROM v_student_full
            WHERE student_id = ?
        """;

        JPanel infoGrid = new JPanel(
            new GridLayout(0, 2, 20, 12)
        );

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    addInfoLabel(infoGrid, "Mã sinh viên:",
                        rs.getString("student_id"));

                    addInfoLabel(infoGrid, "Họ và tên:",
                        rs.getString("full_name"));

                    addInfoLabel(infoGrid, "Ngày sinh:",
                        rs.getString("birth_date"));

                    addInfoLabel(infoGrid, "Giới tính:",
                        rs.getString("gender"));

                    addInfoLabel(infoGrid, "Email:",
                        rs.getString("email"));

                    addInfoLabel(infoGrid, "Số điện thoại:",
                        rs.getString("phone"));

                    addInfoLabel(infoGrid, "Địa chỉ:",
                        rs.getString("address"));

                    addInfoLabel(infoGrid, "Chuyên ngành:",
                        rs.getString("major"));

                    addInfoLabel(infoGrid, "Lớp:",
                        rs.getString("class_name"));

                    addInfoLabel(infoGrid, "GPA:",
                        String.format("%.2f", rs.getDouble("gpa")));

                    panel.add(infoGrid, BorderLayout.CENTER);

                } else {

                    panel.add(
                        new JLabel("Không tìm thấy thông tin sinh viên."),
                        BorderLayout.CENTER
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            panel.add(
                new JLabel("Không thể tải thông tin sinh viên: " + e.getMessage()),
                BorderLayout.CENTER
            );
        }

        return panel;
    }

    // ==========================================
    // [SỬA MỚI] 4. HIỂN THỊ TỪNG THUỘC TÍNH
    // ==========================================

    private void addInfoLabel(
        JPanel panel,
        String title,
        String value
    ) {

        JPanel itemPanel = new JPanel(new BorderLayout(5, 3));

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        JLabel lblValue = new JLabel(
            value == null || value.trim().isEmpty()
                ? "Chưa cập nhật"
                : value
        );

        lblValue.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        itemPanel.add(lblTitle, BorderLayout.NORTH);
        itemPanel.add(lblValue, BorderLayout.CENTER);

        panel.add(itemPanel);
    }

    // ==========================================
    // 5. KIỂM TRA MÃ SINH VIÊN
    // ==========================================

    private void openStudentWindow(
        String studentId,
        String functionName,
        Runnable action
    ) {

        if (studentId == null || studentId.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Tài khoản chưa được liên kết với mã sinh viên!\n"
                    + "Vui lòng liên hệ quản trị viên.",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        action.run();
    }

    // ==========================================
    // 6. TẠO NÚT MENU
    // ==========================================

    private void addMenuButton(
        JPanel panel,
        String title,
        Runnable action
    ) {

        JButton button = new JButton(title);

        button.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        button.setPreferredSize(
            new Dimension(250, 65)
        );

        button.setFocusPainted(false);

        button.addActionListener(e -> action.run());

        panel.add(button);
    }

    // ==========================================
    // 7. ĐĂNG XUẤT
    // ==========================================

    private void logout() {

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {

            new LoginView().setVisible(true);

            dispose();
        }
    }
}
