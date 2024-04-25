package domain.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import ui.screens.SignInView;
import ui.screens.SignUpView;

public class SignUpController {

    
    private SignUpView signUpView;

    public SignUpController(SignUpView view) {
        this.signUpView = view;
        setupListeners();
    }

    private void setupListeners() {
        signUpView.addSignUpButtonListener(e -> register());
        signUpView.addBackButtonListener(e -> showSignInView());
    }

    private void register() {
        String username = signUpView.getUsername();
        String email = signUpView.getEmail();
        String password = signUpView.getPassword(); // Consider hashing this password
        String confirmPassword = signUpView.getConfirmPassword();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signUpView.setStatus("Please fill all fields!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            signUpView.setStatus("Passwords do not match!");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (usernameExists(username, conn)) {
                signUpView.setStatus("Username already exists!");
                return;
            }

            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password); // Hash the password before setting it here
                stmt.executeUpdate();
            }
            signUpView.setStatus("Registration successful!");
        } catch (SQLException e) {
            e.printStackTrace();
            signUpView.setStatus("Database error: " + e.getMessage());
        }
    }
    
    
    private boolean usernameExists(String username, Connection conn) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }


    private void showSignInView() {
        SignInView signInView = new SignInView();
        SignInController controller = new SignInController(signInView);
        signInView.display();
        signUpView.closeFrame();
    }
}
