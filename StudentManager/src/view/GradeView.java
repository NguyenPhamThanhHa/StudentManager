package view;

import javax.swing.*;
public class GradeView extends JFrame {
    public GradeView(String studentID) {
        setTitle("Bảng điểm - MSSV: " + studentID);
        setSize(600, 400);
        setLocationRelativeTo(null);
        add(new JLabel("Trang xem điểm của sinh viên", JLabel.CENTER));
    }
}