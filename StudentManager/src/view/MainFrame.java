
package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Hệ thống Quản lý Sinh viên và đăng kí môn học");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // PHẦN TIÊU ĐỀ
        JLabel lbTitle = new JLabel(
                "Hệ thống Quản lý Sinh viên và đăng kí môn học",
                SwingConstants.CENTER
        );

        lbTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lbTitle.setBorder(
                BorderFactory.createEmptyBorder(20, 0, 20, 0)
        );

        add(lbTitle, BorderLayout.NORTH);

        // PHẦN MENU CHỨC NĂNG
        JPanel panelMenu = new JPanel(new GridLayout(2, 1, 15, 15));
        panelMenu.setBorder(
                BorderFactory.createEmptyBorder(50, 200, 50, 200)
        );

        JButton btnStudentManager =
                new JButton("1. Quản lý sinh viên");

        JButton btnCourseManager =
                new JButton("2. Quản lý học phần");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        btnStudentManager.setFont(buttonFont);
        btnCourseManager.setFont(buttonFont);

        panelMenu.add(btnStudentManager);
        panelMenu.add(btnCourseManager);

        add(panelMenu, BorderLayout.CENTER);

        // XỬ LÝ SỰ KIỆN NÚT QUẢN LÝ SINH VIÊN
        btnStudentManager.addActionListener(e -> {
            StudentView studentView = new StudentView();
            studentView.setVisible(true);
        });

        // XỬ LÝ SỰ KIỆN NÚT QUẢN LÝ HỌC PHẦN
        btnCourseManager.addActionListener(e -> {
            try {
                CourseView courseView = new CourseView();
                courseView.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể mở giao diện quản lý học phần: "
                                + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

    } 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
