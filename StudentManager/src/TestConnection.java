import java.sql.Connection;
import view.DBConnection;

public class TestConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("KET NOI DATABASE THANH CONG!");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("KET NOI DATABASE THAT BAI!");
        }
    }
}