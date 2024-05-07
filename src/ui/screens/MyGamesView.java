package ui.screens;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import domain.models.GameSession;
import ui.screens.BModeUI.GameElement;

public class MyGamesView extends JPanel { 
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton, signUpButton;
    private JLabel statusLabel;
    
    public MyGamesView() {
        this.setLayout(null);
        initUI();
    }
    
    private void initUI() {
    	
    	GameSession g = new GameSession();
        GameElement gameItem = new GameElement(g);
        gameItem.setBounds(50, 150, 250, 100);
        this.add(gameItem);
        JLabel b = new JLabel("bartu");
        b.setBounds(50, 50, 150, 30);
        this.add(b);
        
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
    
//    public void displayUsers(List<User> users) {
//        JTextArea userListTextArea = new JTextArea(10, 30); // Set the size according to your needs
//        for (User user : users) {
//            userListTextArea.append(user.getUsername() + " - " + user.getEmail() + "\n");
//        }
//        // Now display this text area in your UI, either in a dialog or directly in the main window
//        JOptionPane.showMessageDialog(null, userListTextArea, "User List", JOptionPane.INFORMATION_MESSAGE);
//    }
    
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
