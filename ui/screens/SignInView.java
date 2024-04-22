package ui.screens;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignInView extends JPanel {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton, signUpButton;
    private JLabel statusLabel;
    
    public SignInView() {
        this.setLayout(null);
        //this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initUI();
        frameSetup();
    }
    
    private void initUI() {
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 25);
        this.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 25);
        this.add(usernameField);
        
        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 25);
        this.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 25);
        this.add(passwordField);

        // Similar for other components...
        signInButton = new JButton("Sign In");
        signInButton.setBounds(150, 150, 100, 30);
        this.add(signInButton);
        
        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(260, 150, 100, 30);
        this.add(signUpButton);

        statusLabel = new JLabel();
        statusLabel.setBounds(50, 200, 300, 25);
        this.add(statusLabel);
    }
    
    private void frameSetup() {
    	frame = new JFrame("Sign In");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);

        // Set size and location before making the frame visible
        frame.setSize(new Dimension(900, 600));
        frame.setLocationRelativeTo(null);

        // Finally, make the frame visible after all properties are set
        frame.setVisible(true);
    }
    
    public void display() {
        frame.setVisible(true);
    }
    
    public void closeFrame() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();  // Close the frame when switching views
        }
    }
    
    public JPanel getPanel() {
        return this;
    }

    // Getter methods for username and password fields
    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Method to display status message
    public void setSignInStatus(String message) {
        statusLabel.setText(message);
    }

    public void addSignInButtonListener(ActionListener listener) {
        signInButton.addActionListener(listener);
    }
    
    public void addSignUpButtonListener(ActionListener listener) {
        signUpButton.addActionListener(listener);
    }


}
