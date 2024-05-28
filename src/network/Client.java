package network;

// Client.java
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = JOptionPane.showInputDialog(
                "Enter server IP address:");
        String clientName = JOptionPane.showInputDialog(
                "Enter your name:");

        try (Socket socket = new Socket(serverAddress, 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(clientName);

            JFrame frame = new JFrame("Client");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            JTextArea messageArea = new JTextArea();
            messageArea.setEditable(false);
            frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

            JButton readyButton = new JButton("Ready");
            frame.add(readyButton, BorderLayout.SOUTH);

            readyButton.addActionListener(e -> out.println("READY"));

            frame.setVisible(true);

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                messageArea.append(message + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
