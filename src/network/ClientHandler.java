package network;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private boolean ready;
    private PrintWriter out;
    private ServerController controller;

    public ClientHandler(Socket socket, ServerController controller) {
        this.socket = socket;
        this.controller = controller;
        this.ready = false;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);
            String clientName = in.readLine();
            controller.addClientName(clientName); // Add client's name to the right panel

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                } else if (message.equals("READY")) {
                    ready = true;
                    controller.setClientReadyStatus("Ready");
                    controller.checkIfAllReady();
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
