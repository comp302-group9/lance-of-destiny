package network;

import java.io.*;
import java.net.Socket;

public class ClientModel {
    private Socket socket;
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private int[][] grid;

    public ClientModel(String serverAddress, int serverPort, int[][] grid) throws IOException {
        this.grid = grid;
        socket = new Socket(serverAddress, serverPort);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
    }

    public DataOutputStream getToServer() {
        return toServer;
    }

    public DataInputStream getFromServer() {
        return fromServer;
    }

    public Socket getSocket() {
        return socket;
    }

    public int[][] getGrid() {
        return grid;
    }
}

