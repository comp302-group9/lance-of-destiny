package network;

// Server.java
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<ClientHandler> clients = new ArrayList<>();
    private static DefaultListModel<String> clientListModel;
    private static boolean serverReady = false;
    private static JLabel statusLabel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        clientListModel = new DefaultListModel<>();
        JList<String> clientList = new JList<>(clientListModel);
        frame.add(new JScrollPane(clientList), BorderLayout.CENTER);

        JButton readyButton = new JButton("Ready");
        frame.add(readyButton, BorderLayout.SOUTH);

        statusLabel = new JLabel("Waiting for players...");
        frame.add(statusLabel, BorderLayout.NORTH);

        readyButton.addActionListener(e -> {
            serverReady = true;
            checkIfAllReady();
        });

        frame.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void checkIfAllReady() {
        if (serverReady && clients.stream().allMatch(ClientHandler::isReady)) {
            statusLabel.setText("All players are ready!");
            for (ClientHandler client : clients) {
                client.sendMessage("All players are ready!");
            }
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private boolean ready = false;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                out = new PrintWriter(socket.getOutputStream(), true);
                String clientName = in.readLine();
                clientListModel.addElement(clientName);

                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    } else if (message.equals("READY")) {
                        ready = true;
                        checkIfAllReady();
                    }
                    System.out.println("Received from client: " + message);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public boolean isReady() {
            return ready;
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}