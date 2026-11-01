package view;
import javax.swing.*;
public class CourseRegistrationView extends JFrame {
    public CourseRegistrationView(String studentID) {
        setTitle("Đăng ký môn học - MSSV: " + studentID);
        setSize(600, 400);
        setLocationRelativeTo(null);
        add(new JLabel("Trang đăng ký môn học", JLabel.CENTER));
    }
}