package network.chat;

import javax.swing.*;

import domain.controllers.RunningModeController;
import domain.models.RunningModeModel;
import ui.screens.RunningModeView;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private Socket socket;
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private int[][] grid;

    public Client(String serverAddress, int serverPort, int[][] grid) {
        this.grid=grid;
        try {
            socket = new Socket(serverAddress, serverPort);
            toServer = new DataOutputStream(socket.getOutputStream());
            fromServer = new DataInputStream(socket.getInputStream());

            setupUI();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage());
        }
    }

    private void setupUI() {
        JButton readyButton = new JButton("I'm Ready");
        JLabel statusLabel = new JLabel("Not ready");

        setLayout(new FlowLayout());
        add(readyButton);
        add(statusLabel);
        readyButton.addActionListener(e -> {
            try {
                toServer.writeUTF("READY");
                readyButton.setEnabled(false);
                statusLabel.setText("Waiting for other player...");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    new Thread(() -> {
    try {
                String response = fromServer.readUTF();
if ("ALL_READY".equals(response)) {
    // Receive grid configuration from the server
    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
    int[][] receivedGrid = (int[][]) objectInputStream.readObject();
    
    SwingUtilities.invokeLater(() -> {
        statusLabel.setText("All players are ready! Starting...");
        switchToRunningMode(receivedGrid); // Pass received grid to switchToRunningMode method
    });
}
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }).start();

        
        setTitle("Player Client");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    

    private void switchToRunningMode(int[][] grid) {
        // TODO Auto-generated method stub
    // Create the running mode components and switch views
    RunningModeModel runningModeModel = new RunningModeModel();
    RunningModeView runningModeView = new RunningModeView(runningModeModel);
    RunningModeController runningModeController = new RunningModeController(runningModeModel, runningModeView, grid);

    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Running Mode");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(runningModeView);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        runningModeView.requestFocusInWindow();
        Thread gameThread = new Thread(runningModeController);
        gameThread.start();
    });    }

    public static void main(String[] args) {
        new Client("localhost", 1234, null);
    }
}