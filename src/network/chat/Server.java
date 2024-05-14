package network.chat;

import javax.swing.*;

import domain.controllers.RunningModeController;
import domain.models.RunningModeModel;
import ui.screens.RunningModeView;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends JFrame {
    private ServerSocket serverSocket;
    private Socket[] clients = new Socket[2]; // Assuming two players total, including the server
    private DataOutputStream[] toClients = new DataOutputStream[2];
    private DataInputStream[] fromClients = new DataInputStream[2];
    private AtomicInteger readyCount = new AtomicInteger(0);
    private JButton readyButton;
    private JLabel statusLabel;
    private int[][] grid;

    public Server(int port, int[][] grid) throws IOException {
        this.grid = grid;
        serverSocket = new ServerSocket(port);
        setupUI();
        System.out.println("Server started on port " + port);
    }

    private void setupUI() {
        setTitle("Server as Player 1");
        setSize(300, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        readyButton = new JButton("I'm Ready");
        statusLabel = new JLabel("Not Ready");
        add(readyButton);
        add(statusLabel);

        readyButton.addActionListener(e -> handleSelfReady());
        setVisible(true);
    }

    private void handleSelfReady() {
        readyButton.setEnabled(false);
        statusLabel.setText("Server Ready");
        int readyPlayers = readyCount.incrementAndGet();
        if (readyPlayers == clients.length) {
            notifyAllPlayersReady();
        }
    }

    public void execute() {
        try {
            // Accept client connections for other players
            for (int i = 0; i < clients.length - 1 ; i++) { // Accept only one client connection
                clients[i] = serverSocket.accept();
                toClients[i] = new DataOutputStream(clients[i].getOutputStream());
                fromClients[i] = new DataInputStream(clients[i].getInputStream());
                System.out.println("Player " + (i + 1) + " connected.");
                final int playerIndex = i;
                new Thread(() -> handleClient(playerIndex)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyAllPlayersReady() {
        System.out.println("All players are ready. Starting game...");
        for (int i = 0; i < toClients.length - 1; i++) {
            try {
                // Send grid configuration to each client
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClients[i]);
                objectOutputStream.writeObject(grid); // Replace 'gridConfiguration' with your actual grid data
                objectOutputStream.flush();

                // Optionally, send player ID
                // toClients[i].writeInt(playerID); // Replace 'playerID' with a unique identifier for each client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Trigger game start on the server itself
        switchToRunningMode(grid);
    }

    private void handleClient(int playerIndex) {
        try {
            while (true) {
                String message = fromClients[playerIndex].readUTF();
                if ("READY".equals(message)) {
                    System.out.println("Player " + (playerIndex + 1) + " is ready.");
                    int readyPlayers = readyCount.incrementAndGet();
                    if (readyPlayers == clients.length) {
                        notifyAllPlayersReady();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToRunningMode(int[][] grid) {
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
        });
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(1234, null);
        server.execute();
        server.actLikeClient("172.21.171.114", 1234); // Connect to itself as a client
    }

    private void actLikeClient(String serverAddress, int serverPort) {
        try {
            clients[clients.length - 1] = new Socket(serverAddress, serverPort);
            toClients[clients.length - 1] = new DataOutputStream(clients[clients.length - 1].getOutputStream());
            fromClients[clients.length - 1] = new DataInputStream(clients[clients.length - 1].getInputStream());
            handleClient(clients.length - 1);
            readyButton.doClick(); // Simulate click on readyButton to act like a client
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

