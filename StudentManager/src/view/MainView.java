
package view;

import javax.swing.*;
import java.awt.*;

import model.User;

public class MainView extends JFrame {

    private final User user;

    // Constructor
    public MainView(User user) {

        this.user = user;

        setTitle("Hệ thống quản lý sinh viên và đăng ký học phần");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel chính
        JPanel panel = new JPanel(new GridLayout(0, 1, 12, 12));
        panel.setBorder(
            BorderFactory.createEmptyBorder(30, 120, 30, 120)
        );

        // Tiêu đề chào mừng
        JLabel lblWelcome = new JLabel(
            "Xin chào: " + user.getUsername()
                + " (" + user.getRole() + ")",
            JLabel.CENTER
        );

        lblWelcome.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lblWelcome);

        // Hiển thị chức năng theo quyền
        if (user.isAdmin()) {
            createAdminMenu(panel);

        } else if (user.isStudent()) {
            createStudentMenu(panel);

        } else {
            JOptionPane.showMessageDialog(
                this,
                "Tài khoản không có quyền truy cập!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );
        }

        // Nút đăng xuất
        JButton btnLogout = new JButton("Đăng xuất");

        btnLogout.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                new LoginView().setVisible(true);
                dispose();
            }
        });

        panel.add(btnLogout);

        add(panel);
    }

    // ================= ADMIN MENU =================

    private void createAdminMenu(JPanel panel) {

        JLabel lblAdmin = new JLabel(
            "CHỨC NĂNG QUẢN TRỊ",
            JLabel.CENTER
        );

        lblAdmin.setFont(new Font("Arial", Font.BOLD, 17));
        panel.add(lblAdmin);

        JButton btnStudent = new JButton("Quản lý sinh viên");
        JButton btnCourse = new JButton("Quản lý học phần");
        JButton btnSection = new JButton("Quản lý lớp học phần");
        JButton btnSchedule = new JButton("Quản lý lịch học");
        JButton btnRegistration = new JButton("Quản lý đăng ký học phần");

        // Quản lý sinh viên
        btnStudent.addActionListener(
            e -> new StudentView().setVisible(true)
        );

        // Quản lý học phần
        btnCourse.addActionListener(
            e -> new CourseView().setVisible(true)
        );

        // Quản lý lớp học phần
        btnSection.addActionListener(
            e -> new CourseSectionView().setVisible(true)
        );

        // Quản lý lịch học
        btnSchedule.addActionListener(
            e -> new ScheduleView().setVisible(true)
        );

        // Quản lý đăng ký học phần
        btnRegistration.addActionListener(
            e -> new RegistrationView().setVisible(true)
        );

        panel.add(btnStudent);
        panel.add(btnCourse);
        panel.add(btnSection);
        panel.add(btnSchedule);
        panel.add(btnRegistration);
    }

    // ================= STUDENT MENU =================

    private void createStudentMenu(JPanel panel) {

        JLabel lblStudent = new JLabel(
            "CHỨC NĂNG SINH VIÊN",
            JLabel.CENTER
        );

        lblStudent.setFont(new Font("Arial", Font.BOLD, 17));
        panel.add(lblStudent);

        JButton btnRegister = new JButton("Đăng ký học phần");

        JButton btnMyRegistration =
            new JButton("Xem / Hủy học phần đã đăng ký");

        JButton btnSchedule =
            new JButton("Xem thời khóa biểu");

        JButton btnGrade =
            new JButton("Xem bảng điểm");

        // Đăng ký học phần
        btnRegister.addActionListener(
            e -> new CourseRegistrationView(
                user.getStudentId()
            ).setVisible(true)
        );

        // Xem và hủy học phần đã đăng ký
        btnMyRegistration.addActionListener(
            e -> new RegistrationView(
                user.getStudentId()
            ).setVisible(true)
        );

        // Xem thời khóa biểu
        btnSchedule.addActionListener(
            e -> new StudentScheduleView(
                user.getStudentId()
            ).setVisible(true)
        );

        // Xem bảng điểm
        btnGrade.addActionListener(
            e -> new GradeView(
                user.getStudentId()
            ).setVisible(true)
        );

        panel.add(btnRegister);
        panel.add(btnMyRegistration);
        panel.add(btnSchedule);
        panel.add(btnGrade);
    }
}
