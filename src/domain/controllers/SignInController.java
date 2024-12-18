

package domain.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;import database.DatabaseConnection;
import domain.models.BuildingModeModel;
import domain.models.User;
import ui.screens.BuildingModeView;
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
                
                user = new User(username, password);
                BuildingModeModel model2 = new BuildingModeModel(user);
                BuildingModeView view2 = new BuildingModeView(model2);
                BuildingModeController controller2 = new BuildingModeController(model2, view2);
                
                JFrame frame = new JFrame();
                frame.add(view2);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                
                //frame.setSize(SignInPage.WIDTH, SignInPage.HEIGHT);
                frame.setLocationRelativeTo(null);
                
                signInView.closeFrame();
                
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

    private void showSignUpView() { 
        SignUpView signUpView = new SignUpView();
        SignUpController controller = new SignUpController(signUpView);
        signInView.closeFrame();
        //signUpView.setVisible(false);
        //controller.getSignUpView().setVisible(true);
        //controller.getSignInView().setVisible(false);
    }
}