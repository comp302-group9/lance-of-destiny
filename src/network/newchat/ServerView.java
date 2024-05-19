package network.newchat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ServerView extends JPanel {
    private JButton readyButton;
    private JLabel statusLabel;
    private Image backgroundImage;
    private JPanel leftPanelCenter;
    private JPanel rightPanelCenter;
    private JLabel clientNameLabel;
    private JLabel clientStatusLabel;
    private JLabel ipLabel;
    private JLabel allPlayersReadyLabel;

    public ServerView() {
        // Load the background GIF
        ImageIcon backgroundIcon = new ImageIcon("src/ui/gifs/versus.gif");
        backgroundImage = backgroundIcon.getImage();

        // Set layout manager for the main panel
        setLayout(new GridBagLayout());

        // Initialize the buttons and labels
        readyButton = new JButton("I'm Ready");
        statusLabel = new JLabel("Not Ready");
        clientNameLabel = new JLabel("Waiting");
        clientStatusLabel = new JLabel("Not Ready");
        ipLabel = new JLabel();
        ipLabel.setForeground(Color.WHITE); // Set IP label text color to white
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

        // Set up GridBagConstraints for the IP label
        GridBagConstraints gbcIp = new GridBagConstraints();
        gbcIp.insets = new Insets(10, 0, 20, 0); // Padding for the IP label
        gbcIp.gridx = 0;
        gbcIp.gridy = 0;
        gbcIp.gridwidth = 2;
        gbcIp.anchor = GridBagConstraints.CENTER;

        // Add the IP label to the main panel
        add(ipLabel, gbcIp);

        // Set up GridBagConstraints for the left panel
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(0, 0, 80, 250); // Add padding to the right to push the panel left
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 1;
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

        // Add the ready button to the left center panel
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0;
        gbcButton.gridy = 1; // Position the button below the label
        gbcButton.anchor = GridBagConstraints.CENTER;
        leftPanelCenter.add(readyButton, gbcButton);

        // Set up GridBagConstraints for the main panel
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(0, 0, 0, 0);
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
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

        // Add the client's name label to the right center panel
        clientNameLabel.setForeground(Color.WHITE); // White text for visibility
        clientNameLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set the font size to 24
        GridBagConstraints gbcClientName = new GridBagConstraints();
        gbcClientName.gridx = 0;
        gbcClientName.gridy = 0; // Position the label at the top
        gbcClientName.anchor = GridBagConstraints.CENTER;
        rightPanelCenter.add(clientNameLabel, gbcClientName);

        // Add the client status label below the client's name label
        GridBagConstraints gbcClientStatusLabel = new GridBagConstraints();
        gbcClientStatusLabel.gridx = 0;
        gbcClientStatusLabel.gridy = 1; // Position the status label below the name label
        gbcClientStatusLabel.anchor = GridBagConstraints.CENTER;
        rightPanelCenter.add(clientStatusLabel, gbcClientStatusLabel);

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

    public void setLeftPanelCenterText(String text) {
        for (Component component : leftPanelCenter.getComponents()) {
            if (component instanceof JLabel) {
                ((JLabel) component).setText(text);
            }
        }
    }

    public void setRightPanelCenterText(String text) {
        clientNameLabel.setText(text);
    }

    public void setClientStatusLabelText(String text) {
        clientStatusLabel.setText(text);
    }

    public void setIpLabelText(String text) {
        ipLabel.setText("Server IP: " + text);
    }

    public void setAllPlayersReadyText(String text) {
        allPlayersReadyLabel.setText(text);
    }
}
