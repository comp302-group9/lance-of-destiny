package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// APPLICATION OF SINGLETON PATTERN
// Why is this a Singleton class?
// 1. Its constructor is private to prevent instantiation of the class from outside.
// 2. It has a private static variable that holds the singleton instance of the class.
// 3. It has a public static method that returns this instance or creates it if necessary.


public class DatabaseConnection {
    private static Connection instance = null;
    private static final String url = "jdbc:mysql://lance-of-destiny.cfk044iesems.eu-north-1.rds.amazonaws.com/lance-of-destiny";
    private static final String user = "admin";
    private static final String password = "yp86xb#$Au";

    // Private constructor to prevent instantiation from outside the class
    private DatabaseConnection() {
    }

    // Public method to provide access to the singleton instance
    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            try {
                instance = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                System.out.println("Error creating connection: " + e.getMessage());
                throw e;
            }
        }
        return instance;
    }
}