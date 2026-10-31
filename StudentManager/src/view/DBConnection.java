package view;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8";

    private static final String USER = "root";

    // Thay bằng mật khẩu MySQL trên máy của bạn
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}