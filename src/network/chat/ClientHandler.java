package network.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private ObjectInputStream inputStream;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            String clientName = (String) inputStream.readObject(); // Read client name immediately after connection
            server.addClientName(clientName); // Add client name to the server's list
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Keep the connection open, read messages, etc.
            // This example will just sleep to simulate an open connection
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Handle client disconnection here
            // You might want to remove the client name from the list here if disconnections are to be handled
        }
    }
}
