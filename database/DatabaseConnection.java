package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String url = "jdbc:mysql://lance-of-destiny.cfk044iesems.eu-north-1.rds.amazonaws.com/lance-of-destiny";
    private static String user = "admin";
    private static String password = "yp86xb#$Au";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}