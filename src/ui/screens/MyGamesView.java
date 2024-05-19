package ui.screens;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ui.screens.BModeUI.GameElement;

public class MyGamesView extends JPanel { 
    private JFrame frame;
    private JPanel gamesPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JScrollPane scrollPane;
    private JButton backButton;
    private JLabel statusLabel;
    private int yOffset = 150;
    private int numberOfGameElements = 0;
    
    public MyGamesView() {
        this.setLayout(null);
        initUI();
    }
    
    private void initUI() {
        // Dimension calculations
        int width = 600 * 16 / 9;
        int height = 600; // Assuming the height you want for the frame

        gamesPanel = new JPanel();
        gamesPanel.setLayout(null); // Keep null layout for absolute positioning
        gamesPanel.setPreferredSize(new Dimension(width, 2000)); // Prefer a large height to start with

        // Set up the scroll pane
        scrollPane = new JScrollPane(gamesPanel);
        scrollPane.setBounds(0, 0, width, height); // Match the scroll pane size to the frame's content area
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane);

        // Sample label
        JLabel b = new JLabel("bartu");
        b.setBounds(50, 50, 150, 30);
        gamesPanel.add(b); // Adding label to gamesPanel so it's within the scrollable area

        // Back button setup
        backButton = new JButton("back");
        backButton.setBounds(50, 100, 150, 30);
        this.add(backButton); // Adding directly to this JPanel, not gamesPanel

        // Frame setup, if necessary here
        frame = new JFrame("My Games");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setSize(width, height); // Set frame size to match your desired aspect ratio and height
    }

    public void addGameElement(GameElement gameElement, int gameIndex) {
        int yPosition = yOffset + (gameIndex - 1) * 110; // Calculate vertical position
        gameElement.setBounds(50, yPosition, 250, 100); // Set bounds within gamesPanel
        gamesPanel.add(gameElement);
        numberOfGameElements++;
        updateScrollPane(); // Adjust gamesPanel's preferred size based on added elements
    }

    private void updateScrollPane() {
        int newHeight = yOffset + numberOfGameElements * 110 + 50; // Adjust height dynamically based on content
        gamesPanel.setPreferredSize(new Dimension(250, newHeight));
        gamesPanel.revalidate();
        gamesPanel.repaint();
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
    
//    public void addGameElement(GameElement gameElement, int gameIndex) {
//        int yPosition = yOffset + (gameIndex - 1) * 110; // Calculate the vertical position
//        gameElement.setBounds(50, yPosition, 250, 100); // Set bounds with dynamic Y
//        this.add(gameElement);
//        this.revalidate();
//        this.repaint();
//    }
    
//    public void addGameElement(GameElement gameElement, int gameIndex) {
//        int yPosition = yOffset + (gameIndex - 1) * 110; // Calculate vertical position
//        gameElement.setBounds(0, yPosition, 250, 100); // Set bounds within gamesPanel
//        gamesPanel.add(gameElement);
//        numberOfGameElements++;
//        updateScrollPane(); // Update scroll pane's preferred size
//    }
//    
//    
//    private void updateScrollPane() {
//        int newHeight = yOffset + numberOfGameElements * 110 + 50; // Adjust height based on content
//        gamesPanel.setPreferredSize(new Dimension(250, newHeight));
//        gamesPanel.revalidate();
//        gamesPanel.repaint();
//    }
    
//    public void displayUsers(List<User> users) {
//        JTextArea userListTextArea = new JTextArea(10, 30); // Set the size according to your needs
//        for (User user : users) {
//            userListTextArea.append(user.getUsername() + " - " + user.getEmail() + "\n");
//        }
//        // Now display this text area in your UI, either in a dialog or directly in the main window
//        JOptionPane.showMessageDialog(null, userListTextArea, "User List", JOptionPane.INFORMATION_MESSAGE);
//    }
    
    public JPanel getPanel() {
        return gamesPanel;
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

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

}
