package network.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ServerControlPanel extends JPanel {
    private JButton startServerButton;
    private JButton stopServerButton;
    private JLabel statusLabel;
    private Server server;
    private Thread serverThread;

    public ServerControlPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        startServerButton = new JButton("Start Server");
        stopServerButton = new JButton("Stop Server");
        statusLabel = new JLabel("Server not running.");

        add(startServerButton, BorderLayout.NORTH);
        add(stopServerButton, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.CENTER);

        startServerButton.addActionListener(e -> startServer());
        stopServerButton.addActionListener(e -> stopServer());

        stopServerButton.setEnabled(false); // Server is not running initially
    }

    private void startServer() {
        if (serverThread == null || !serverThread.isAlive()) {
            try {
                server = new Server(1234);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            serverThread = new Thread(() -> {
                server.execute();
                SwingUtilities.invokeLater(() -> statusLabel.setText("Server running."));
            });
            serverThread.start();
            startServerButton.setEnabled(false);
            stopServerButton.setEnabled(true);
        }
    }

    private void stopServer() {
        if (server != null) {
            server.stop(); // You need to implement this method in the Server class
            serverThread.interrupt();
            startServerButton.setEnabled(true);
            stopServerButton.setEnabled(false);
            statusLabel.setText("Server stopped.");
        }
    }
}
