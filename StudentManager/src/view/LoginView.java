
package view;

import javax.swing.*;
import java.awt.*;

import Data_Access_Object.userDAO;
import model.User;

public class LoginView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private final userDAO userDAO = new userDAO();

    public LoginView() {

        setTitle("Đăng nhập hệ thống quản lý sinh viên");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JButton btnLogin = new JButton("Đăng nhập");

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));
        panel.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        panel.add(new JLabel("Tài khoản:"));
        panel.add(txtUsername);

        panel.add(new JLabel("Mật khẩu:"));
        panel.add(txtPassword);

        panel.add(new JLabel(""));
        panel.add(btnLogin);

        add(lblTitle, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
    }

    private void login() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Vui lòng nhập đầy đủ tài khoản và mật khẩu!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        User user = userDAO.login(username, password);

        if (user == null) {
            JOptionPane.showMessageDialog(
                this,
                "Tài khoản hoặc mật khẩu không chính xác!",
                "Đăng nhập thất bại",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (user.isStudent() &&
                (user.getStudentId() == null ||
                 user.getStudentId().isBlank())) {
            JOptionPane.showMessageDialog(
                this,
                "Tài khoản sinh viên chưa được liên kết mã sinh viên!",
                "Lỗi tài khoản",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        new MainView(user).setVisible(true);
        dispose();
    }
}
