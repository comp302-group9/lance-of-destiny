package domain.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import domain.models.User;
import ui.screens.SignInView;
import ui.screens.SignUpView;

public class SignInController {

    private User user;
    private SignInView signInView;

    public SignInController(SignInView view) {
        this.signInView = view;

        setupListeners(); 
    }

    private void setupListeners() {
        signInView.addSignInButtonListener(e -> signIn());
        signInView.addSignUpButtonListener(e -> showSignUpView());
    }

    private void signIn() {
        String username = signInView.getUsername();
        String password = signInView.getPassword();

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (validateCredentials(username, password, conn)) {
                signInView.setSignInStatus("Sign in successful.");
                //displayUsers(conn);
            } else {
                signInView.setSignInStatus("Invalid credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            signInView.setSignInStatus("Database error: " + e.getMessage());
        }
    }
    
    private boolean validateCredentials(String username, String password, Connection conn) throws SQLException {
        String sql = "SELECT password FROM Users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return password.equals(storedPassword); // Replace with password hash comparison
                }
            }
        }
        return false;
    }
    
//    private List<User> fetchAllUsers(Connection conn) throws SQLException {
//        List<User> users = new ArrayList<>();
//        String sql = "SELECT username, email FROM users";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    String username = rs.getString("username");
//                    String email = rs.getString("email");
//                    users.add(new User(username, email)); // Assuming User has a constructor that accepts username and email
//                }
//            }
//        }
//        return users;
//    }
//    
//    private void displayUsers(Connection conn) throws SQLException {
//        List<User> users = fetchAllUsers(conn);
//        // Assuming you have a method in signInView to display these users:
//        signInView.displayUsers(users);
//    }

    private void showSignUpView() { 
        SignUpView signUpView = new SignUpView();
        SignUpController controller = new SignUpController(signUpView);
        signInView.closeFrame();
        //signUpView.setVisible(false);
        //controller.getSignUpView().setVisible(true);
        //controller.getSignInView().setVisible(false);
    }
}
