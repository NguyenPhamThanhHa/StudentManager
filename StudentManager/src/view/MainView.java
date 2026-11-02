
package view;

import javax.swing.*;
import java.awt.*;

import model.User;

public class MainView extends JFrame {

    private User user;

    public MainView(User user) {

        this.user = user;

        setTitle("Hệ thống quản lý sinh viên và đăng ký học phần");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    // ================= KHỞI TẠO GIAO DIỆN =================

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        // TIÊU ĐỀ

        JLabel lblTitle = new JLabel(
                "HỆ THỐNG QUẢN LÝ SINH VIÊN",
                JLabel.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 25));
        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 15, 10)
        );

        add(lblTitle, BorderLayout.NORTH);

        // THÔNG TIN TÀI KHOẢN

        JPanel userPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JLabel lblUsername = new JLabel(
                "Xin chào: " + user.getUsername(),
                JLabel.CENTER
        );

        JLabel lblRole = new JLabel(
                "Vai trò: " + (user.isAdmin() ? "Quản trị viên" : "Sinh viên"),
                JLabel.CENTER
        );

        lblUsername.setFont(new Font("Arial", Font.BOLD, 16));
        lblRole.setFont(new Font("Arial", Font.PLAIN, 14));

        userPanel.add(lblUsername);
        userPanel.add(lblRole);

        add(userPanel, BorderLayout.NORTH);

        // MENU CHỨC NĂNG

        JPanel menuPanel = new JPanel(
                new GridLayout(0, 2, 20, 20)
        );

        menuPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 50, 25, 50)
        );

        if (user.isAdmin()) {

            // CHỨC NĂNG ADMIN

            addMenuButton(menuPanel, "Quản lý sinh viên", () -> {
                new StudentView().setVisible(true);
            });

            addMenuButton(menuPanel, "Quản lý môn học", () -> {
                new CourseView().setVisible(true);
            });

            addMenuButton(menuPanel, "Quản lý mở lớp theo học kỳ", () -> {
                new CourseSectionView().setVisible(true);
            });

            addMenuButton(menuPanel, "Quản lý lịch giảng dạy", () -> {
                new ScheduleView().setVisible(true);
            });

            addMenuButton(menuPanel, "Quản lý đăng ký học phần", () -> {
                new RegistrationView().setVisible(true);
            });

        } else {

            // CHỨC NĂNG SINH VIÊN

            addMenuButton(menuPanel, "Đăng ký học phần", () -> {
                new CourseRegistrationView(user.getStudentId()).setVisible(true);
            });

            addMenuButton(menuPanel, "Lịch sử đăng ký học phần", () -> {
                new RegistrationView(user.getStudentId()).setVisible(true);
            });

            addMenuButton(menuPanel, "Thời khóa biểu", () -> {
                new StudentScheduleView(user.getStudentId()).setVisible(true);
            });

            addMenuButton(menuPanel, "Xem điểm", () -> {
                new GradeView(user.getStudentId()).setVisible(true);
            });
        }

        add(menuPanel, BorderLayout.CENTER);

        // ĐĂNG XUẤT

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnLogout = new JButton("Đăng xuất");

        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));

        btnLogout.addActionListener(e -> logout());

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 15, 15)
        );

        bottomPanel.add(btnLogout);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ================= TẠO NÚT MENU =================

    private void addMenuButton(
            JPanel panel,
            String title,
            Runnable action
    ) {

        JButton button = new JButton(title);

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(250, 65));
        button.setFocusPainted(false);

        button.addActionListener(e -> action.run());

        panel.add(button);
    }

    // ================= ĐĂNG XUẤT =================

    private void logout() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            new LoginView().setVisible(true);

            dispose();
        }
    }
}
