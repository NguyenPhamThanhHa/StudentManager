package view;
import javax.swing.*;
public class ScheduleView extends JFrame {
    public ScheduleView(String studentID) {
        setTitle("Thời khóa biểu - MSSV: " + studentID);
        setSize(600, 400);
        setLocationRelativeTo(null);
        add(new JLabel("Trang xem thời khóa biểu", JLabel.CENTER));
    }
}