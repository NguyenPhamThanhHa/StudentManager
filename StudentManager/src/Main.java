import view.LoginView; // Thay thế cho MainFrame
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Đồng bộ giao diện theo hệ điều hành
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Khởi chạy màn hình đăng nhập trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}