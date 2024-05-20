package ui.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


import java.awt.event.MouseEvent;

import domain.models.User;

public class SignInView extends JPanel { 
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton, signUpButton;
    private JLabel statusLabel;
    private Image backgroundImage;
    
    public SignInView() {
        this.setLayout(null);
        //this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initUI();
        frameSetup();
        backgroundImage = new ImageIcon("src/ui/gifs/back.gif").getImage();
    }

    public JFrame getFrame(){
        return this.frame;
    }
    
    private void initUI() {
        Color original = new Color(173, 206, 235);
        Color oncolor = new Color(173, 206, 255,200);
        LineBorder offBord = new LineBorder(Color.black,1);
        LineBorder onBord = new LineBorder(Color.black, 3);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setBounds(290, 210, 100, 25);
        this.add(usernameLabel); 

        usernameField = new JTextField();
        usernameField.setFont(new Font("Bauhaus 93", Font.ROMAN_BASELINE, 14));
        usernameField.setBounds(370, 210, 200, 25);
        usernameField.setBorder(new LineBorder(Color.BLACK)); // 2 is the thickness
        //usernameField.setBackground(original);
        usernameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                usernameField.setBorder(new LineBorder(Color.BLACK, 2));
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // Reset border thickness when the key is released
                usernameField.setBorder(new LineBorder(Color.BLACK, 1));
            }
        });
        this.add(usernameField);
        
        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBounds(290, 260, 100, 25);
        this.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Bauhaus 93", Font.PLAIN, 14));
        passwordField.setBounds(370, 260, 200, 25);
        passwordField.setBorder(new LineBorder(Color.BLACK));
        //passwordField.setBackground(original);
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                passwordField.setBorder(new LineBorder(Color.BLACK, 2));
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // Reset border thickness when the key is released
                passwordField.setBorder(new LineBorder(Color.BLACK));
            }
        });
        this.add(passwordField);

        // Similar for other components...
        signInButton = new JButton("Sign In");
        signInButton.setBounds(370, 310, 100, 30);
        
        signInButton.setBackground(original);
        signInButton.setBorder(offBord);
        signInButton.setForeground(Color.BLACK);
        signInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Change background to white when mouse enters
                signInButton.setBackground(oncolor);
                signInButton.setBorder(onBord);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Revert background to original color when mouse exits
                signInButton.setBackground(original);
                signInButton.setBorder(offBord);
            }
        });
        this.add(signInButton);
        
        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(480, 310, 100, 30);
        signUpButton.setBackground(original);
        signUpButton.setBorder(offBord);
        signUpButton.setForeground(Color.BLACK);
        signUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Change background to white when mouse enters
                signUpButton.setBackground(oncolor);
                signUpButton.setBorder(onBord);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Revert background to original color when mouse exits
                signUpButton.setBackground(original);
                signUpButton.setBorder(offBord);
            }
        });
        this.add(signUpButton);

        statusLabel = new JLabel();
        statusLabel.setBounds(270, 360, 300, 25);
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
    
    public void displayUsers(List<User> users) {
        JTextArea userListTextArea = new JTextArea(10, 30); // Set the size according to your needs
        for (User user : users) {
            userListTextArea.append(user.getUsername() + " - " + user.getEmail() + "\n");
        }
        // Now display this text area in your UI, either in a dialog or directly in the main window
        JOptionPane.showMessageDialog(null, userListTextArea, "User List", JOptionPane.INFORMATION_MESSAGE);
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
 // Only for testing!!
    public void setUsername(String username) {
        usernameField.setText(username);
    }

    public void setPassword(String password) {
        passwordField.setText(password);
    }

    public String getSignInStatus() {
        return statusLabel.getText();
    }

    public JButton getSignInButton() {
        return signInButton;
    }

    public JButton getSignUpButton() {
        return signUpButton;
    }
    public JLabel getStatusLabel() {
    	return this.statusLabel;
    }
    public JTextField getUsernameField() {
    	return this.usernameField;
    }
    public JTextField getPasswordField() {
    	return this.usernameField;
    }
}
