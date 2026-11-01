package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";

    private static final String USER = "root";
    private static final String PASSWORD = "1911";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("KET NOI MYSQL THANH CONG!");

            return conn;

        } catch (SQLException e) {
            System.out.println("KET NOI MYSQL THAT BAI!");
            System.out.println("Ma loi: " + e.getErrorCode());
            System.out.println("Chi tiet: " + e.getMessage());

            return null;
        }
    }
}