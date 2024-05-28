package network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClientView extends JPanel {
    private JButton readyButton;
    private JLabel statusLabel;
    private Image backgroundImage;
    private JPanel leftPanelCenter;
    private JPanel rightPanelCenter;
    private JLabel serverStatusLabel;
    private JLabel allPlayersReadyLabel;

    public ClientView() {
        // Load the background GIF
        ImageIcon backgroundIcon = new ImageIcon("src/ui/gifs/versus.gif");
        backgroundImage = backgroundIcon.getImage();

        // Set layout manager for the main panel
        setLayout(new GridBagLayout());

        // Initialize the buttons and labels
        readyButton = new JButton("I'm Ready");
        statusLabel = new JLabel("Not Ready");
        serverStatusLabel = new JLabel("Not Ready");
        allPlayersReadyLabel = new JLabel();
        allPlayersReadyLabel.setForeground(Color.WHITE); // Set all players ready label text color to white

        // Create a panel for the left half
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false); // Make the panel transparent to show the background

        // Create a panel for the right half
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false); // Make the panel transparent to show the background

        // Initialize the left center panel
        leftPanelCenter = new JPanel(new GridBagLayout());
        leftPanelCenter.setOpaque(true);
        leftPanelCenter.setBackground(new Color(200, 200, 200, 128)); // Semi-transparent gray
        leftPanelCenter.setPreferredSize(new Dimension(200, 200)); // Set the size to make it a square

        // Initialize the right center panel
        rightPanelCenter = new JPanel(new GridBagLayout());
        rightPanelCenter.setOpaque(true);
        rightPanelCenter.setBackground(new Color(200, 200, 200, 128)); // Semi-transparent gray
        rightPanelCenter.setPreferredSize(new Dimension(200, 200)); // Set the size to make it a square

        // Set up GridBagConstraints for the left panel
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(0, 0, 80, 250); // Add padding to the right to push the panel left
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.anchor = GridBagConstraints.CENTER;

        // Add the left center panel to the left panel
        leftPanel.add(leftPanelCenter, gbcLeft);

        // Add a placeholder label to the left center panel
        JLabel leftPlaceholderLabel = new JLabel("Server");
        leftPlaceholderLabel.setForeground(Color.WHITE); // White text for visibility
        leftPlaceholderLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set the font size to 24
        GridBagConstraints gbcLeftLabel = new GridBagConstraints();
        gbcLeftLabel.gridx = 0;
        gbcLeftLabel.gridy = 0; // Position the label at the top
        gbcLeftLabel.anchor = GridBagConstraints.CENTER;
        leftPanelCenter.add(leftPlaceholderLabel, gbcLeftLabel);

        // Add the server status label below the placeholder label
        GridBagConstraints gbcServerStatusLabel = new GridBagConstraints();
        gbcServerStatusLabel.gridx = 0;
        gbcServerStatusLabel.gridy = 1; // Position the status label below the placeholder label
        gbcServerStatusLabel.anchor = GridBagConstraints.CENTER;
        leftPanelCenter.add(serverStatusLabel, gbcServerStatusLabel);

        // Set up GridBagConstraints for the main panel
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(0, 0, 0, 0);
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.weightx = 0.5;
        gbcMain.weighty = 1;
        gbcMain.fill = GridBagConstraints.BOTH;

        // Add the left panel to the main panel
        add(leftPanel, gbcMain);

        // Set up GridBagConstraints for the right panel
        gbcMain.gridx = 1;
        add(rightPanel, gbcMain);

        // Set up GridBagConstraints for the right center panel
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(0, 250, 80, 0); // Add padding to the left to push the panel right
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.anchor = GridBagConstraints.CENTER;

        // Add the right center panel to the right panel
        rightPanel.add(rightPanelCenter, gbcRight);

        // Add a placeholder label to the right center panel
        JLabel rightPlaceholderLabel = new JLabel("Client");
        rightPlaceholderLabel.setForeground(Color.WHITE); // White text for visibility
        rightPlaceholderLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set the font size to 24
        GridBagConstraints gbcRightLabel = new GridBagConstraints();
        gbcRightLabel.gridx = 0;
        gbcRightLabel.gridy = 0; // Position the label at the top
        gbcRightLabel.anchor = GridBagConstraints.CENTER;
        rightPanelCenter.add(rightPlaceholderLabel, gbcRightLabel);

        // Add the ready button to the right center panel
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0;
        gbcButton.gridy = 1; // Position the button below the label
        gbcButton.anchor = GridBagConstraints.CENTER;
        rightPanelCenter.add(readyButton, gbcButton);

        // Add the client status label below the button
        GridBagConstraints gbcStatusLabel = new GridBagConstraints();
        gbcStatusLabel.gridx = 0;
        gbcStatusLabel.gridy = 2; // Position the status label below the button
        gbcStatusLabel.anchor = GridBagConstraints.CENTER;
        rightPanelCenter.add(statusLabel, gbcStatusLabel);

        // Set up GridBagConstraints for the all players ready label
        GridBagConstraints gbcAllReady = new GridBagConstraints();
        gbcAllReady.insets = new Insets(20, 0, 10, 0); // Padding for the all players ready label
        gbcAllReady.gridx = 0;
        gbcAllReady.gridy = 2;
        gbcAllReady.gridwidth = 2;
        gbcAllReady.anchor = GridBagConstraints.CENTER;

        // Add the all players ready label to the main panel
        add(allPlayersReadyLabel, gbcAllReady);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Draw the background image, scaled to fit the panel
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public void addReadyButtonListener(ActionListener listener) {
        readyButton.addActionListener(listener);
    }

    public void setReadyButtonEnabled(boolean enabled) {
        readyButton.setEnabled(enabled);
    }

    public void setStatusLabelText(String text) {
        statusLabel.setText(text);
    }

    public void setServerStatusLabelText(String text) {
        serverStatusLabel.setText(text);
    }

    public void setLeftPanelCenterText(String text) {
        for (Component component : leftPanelCenter.getComponents()) {
            if (component instanceof JLabel) {
                ((JLabel) component).setText(text);
            }
        }
    }

    public void setRightPanelCenterText(String text) {
        for (Component component : rightPanelCenter.getComponents()) {
            if (component instanceof JLabel) {
                ((JLabel) component).setText(text);
            }
        }
    }

    public void setAllPlayersReadyText(String text) {
        allPlayersReadyLabel.setText(text);
    }
}