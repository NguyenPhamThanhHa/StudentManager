package Data_Access_Object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import view.DBConnection;
import model.User;

public class userDAO {

    public User login(String username, String password) {

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.out.println("KHONG KET NOI DUOC DATABASE!");
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, username.trim());
                ps.setString(2, password.trim());

                System.out.println("Username dang nhap: [" + username + "]");
                System.out.println("Password dang nhap: [" + password + "]");

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        System.out.println("TIM THAY TAI KHOAN!");
                        System.out.println("Role: " + rs.getString("role"));

                        return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("student_id")
                        );
                    } else {
                        System.out.println("KHONG TIM THAY TAI KHOAN!");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}