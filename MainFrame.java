package StudentManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Hệ thống Quản lý Sinh viên và đăng kí môn học");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
//====PHẦN TIÊU ĐỀ=====
        JLabel lbTitle = new JLabel("Hệ thống Quản lý Sinh viên và đăng kí môn học", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lbTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lbTitle, BorderLayout.NORTH);
    

   //======PHẦN MENU CHỨC NĂNG=====
   JPanel panelMenu= new JPanel(new GridLayout(3,1,15,15));
   JButton btnStudentManager = new JButton("1. Quản lý sinh viên");
   JButton btnCourseManager= new JButton("2.Quản lý môn học");
    JButton btnRegistrationManager = new JButton("3. Quản lý đăng kí môn học");

    //đinhk dạng front chữ cho button
    Font buttonFont = new Font("Arial", Font.BOLD, 14);
   btnStudentManager.setFont(buttonFont);
   btnCourseManager.setFont(buttonFont);
   btnRegistrationManager.setFont(buttonFont);

   //thêm button vào panel
   panelMenu.add(btnStudentManager);
   panelMenu.add(btnCourseManager);
   panelMenu.add(btnRegistrationManager);

   //thêm panel vào giữa cửa sổ
   add(panelMenu, BorderLayout.CENTER);
   
   //xử lý sự kiện
   btnStudentManager.addActionListener(e -> {
       StudentView studentView = new StudentView();
   });
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
       new MainFrame().setVisible(true);
    });
}
}