package view;

import javax.swing.*;
import java.awt.*;


import Data_Access_Object.userDAO;
import model.User;

public class LoginView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private userDAO userDAO = new userDAO();

    public LoginView() {

        setTitle("Đăng nhập hệ thống quản lý sinh viên");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JButton btnLogin = new JButton("Đăng nhập");

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
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            return;
        }

        User user = userDAO.login(username, password);

        if (user != null) {

            JOptionPane.showMessageDialog(this,
                "Đăng nhập thành công!");

            new MainView(user).setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this,
                "Tài khoản hoặc mật khẩu không chính xác!");
        }
    }

public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName()
        );
    } catch (Exception e) {
        e.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> {
        new LoginView().setVisible(true);
    });
}
}