// APPLICATION OF SINGLETON PATTERN
// Why is this a Singleton class?
// 1. Its constructor is private to prevent instantiation of the class from outside.
// 2. It has a private static variable that holds the singleton instance of the class.
// 3. It has a public static method that returns this instance or creates it if necessary.


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