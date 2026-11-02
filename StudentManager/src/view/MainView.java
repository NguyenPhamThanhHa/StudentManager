
package view;

import javax.swing.*;
import java.awt.*;

import model.User;

public class MainView extends JFrame {

    private User user;

    // ==========================================
    // 1. CONSTRUCTOR
    // ==========================================

    public MainView(User user) {

        this.user = user;

        setTitle("Hệ thống quản lý sinh viên và đăng ký học phần");
        setSize(850, 600);
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
                "Quản lý mở lớp theo học kỳ",
                () -> new CourseSectionView().setVisible(true)
            );

            addMenuButton(
                menuPanel,
                "Quản lý lịch giảng dạy",
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
            // CHỨC NĂNG SINH VIÊN
            // ==================================

            String studentId = user.getStudentId();

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
        }

        add(menuPanel, BorderLayout.CENTER);

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
    // 3. KIỂM TRA MÃ SINH VIÊN
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
    // 4. TẠO NÚT MENU
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
    // 5. ĐĂNG XUẤT
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
