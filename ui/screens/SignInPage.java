package ui.screens;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SignInPage extends JPanel {
	public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    private JTextField usernameField;
    private JTextField passwordField;
    private JButton signInButton;
    private JLabel statusLabel;
    
    public SignInPage() {
        initUI();
    }
    
    private void initUI() {
        setLayout(null); // For simplicity, using absolute positioning

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 25);
        add(usernameField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 25);
        add(passwordLabel);

        passwordField = new JTextField();
        passwordField.setBounds(150, 100, 200, 25);
        add(passwordField);

        // Sign In button
        signInButton = new JButton("Sign In");
        signInButton.setBounds(150, 150, 100, 30);
        add(signInButton);

        // Status label
        statusLabel = new JLabel();
        statusLabel.setBounds(50, 200, 300, 25);
        add(statusLabel);
    }

    // Getter methods for username and password fields
    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    // Method to display status message
    public void showStatusMessage(String message) {
        statusLabel.setText(message);
    }

}
