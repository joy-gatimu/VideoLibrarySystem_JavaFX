package database_package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() {
        // UPDATE THESE WITH YOUR MYSQL CREDENTIALS
        String url = "jdbc:mysql://localhost:3306/video_library_db?useSSL=false&serverTimezone=UTC";
        String user = "root";      // Your MySQL username
        String password = "";      // Your MySQL password (if none, leave empty)

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }

    // Test connection
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Database connection successful!");
        } else {
            System.out.println("Database connection failed!");
        }
    }
}