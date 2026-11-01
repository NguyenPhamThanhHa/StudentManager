
package view;

import javax.swing.*;
import java.awt.*;
import model.User;

public class MainView extends JFrame {

    private User user;

    public MainView(User user) {
        this.user = user;

        setTitle("Hệ thống quản lý sinh viên");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(
            BorderFactory.createEmptyBorder(25, 100, 25, 100)
        );

        JLabel lblWelcome = new JLabel(
            "Xin chào: " + user.getUsername()
            + " (" + user.getRole() + ")",
            JLabel.CENTER
        );

        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblWelcome);

        if ("ADMIN".equals(user.getRole())) {

            JButton btnStudent = new JButton("Quản lý sinh viên");
            JButton btnCourse = new JButton("Quản lý học phần");

            btnStudent.addActionListener(e ->
                new StudentView().setVisible(true)
            );
            btnCourse.addActionListener(e ->
             new CourseView().setVisible(true)
            );
            panel.add(btnStudent);
            panel.add(btnCourse);

        } else {

            JButton btnRegister =
                new JButton("Đăng ký / Hủy học phần");

            JButton btnSchedule =
                new JButton("Xem thời khóa biểu");

            JButton btnGrade =
                new JButton("Xem điểm");

            btnRegister.addActionListener(e ->
                new CourseRegistrationView(
                    user.getStudentID()
                ).setVisible(true)
            );

            btnSchedule.addActionListener(e ->
                new ScheduleView(
                    user.getStudentID()
                ).setVisible(true)
            );

            btnGrade.addActionListener(e ->
                new GradeView(
                    user.getStudentID()
                ).setVisible(true)
            );

            panel.add(btnRegister);
            panel.add(btnSchedule);
            panel.add(btnGrade);
        }

        JButton btnLogout = new JButton("Đăng xuất");

        btnLogout.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });

        panel.add(btnLogout);

        add(panel);
    }
}
