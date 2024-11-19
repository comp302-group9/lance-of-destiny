package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Updated fields for Azure SQL Database
    private static String url = "jdbc:sqlserver://lance-of-destiny.database.windows.net:1433;database=lance-of-destiny;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private static String user = "sqlconnection@lance-of-destiny"; // Azure SQL username
    private static String password = "yp86xb#$Au"; // Replace with your actual password

    // Private constructor to prevent instantiation (Singleton Pattern)
    private DatabaseConnection() {}

    // Method to get the database connection
    public static Connection getConnection() throws SQLException {
        // Ensure the JDBC driver is loaded
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found.", e);
        }
        
        return DriverManager.getConnection(url, user, password);
    }
}
