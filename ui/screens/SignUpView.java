package ui.screens;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import domain.models.User;

public class SignUpView extends JPanel {
	public static final int WIDTH = 900;
    public static final int HEIGHT = 600;
	
	private JFrame frame;
	private User user;
	private JTextField usernameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signUpButton, backButton;
    private JLabel statusLabel;

    public SignUpView() {
    	this.setLayout(null);
    	this.setPreferredSize(new Dimension(900, 600));
        initUI();
        frameSetup();
    }
    
    private void initUI() {

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(190, 50, 200, 25);
        add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 100, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(190, 100, 200, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 150, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(190, 150, 200, 25);
        add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(50, 200, 150, 25);
        add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(190, 200, 200, 25);
        add(confirmPasswordField);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(160, 250, 100, 30);
        add(signUpButton);

        backButton = new JButton("Back");
        backButton.setBounds(270, 250, 100, 30);
        add(backButton);

        statusLabel = new JLabel();
        statusLabel.setBounds(50, 300, 400, 25);
        add(statusLabel);
    }
    
    private void frameSetup() {
        frame = new JFrame("Sign Up");
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(900, 600));
        frame.setLocationRelativeTo(null); // Center the frame
    }

    public void display() {
        frame.setVisible(true);
    }

    public void addSignUpButtonListener(ActionListener listener) {
        signUpButton.addActionListener(listener);
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
    
    public JPanel getPanel() {
        return this;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
    
    public void closeFrame() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();  // Close the frame when switching views
        }
    }
}
