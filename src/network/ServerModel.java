package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerModel {
    private ServerSocket serverSocket;
    private Socket[] clients = new Socket[2];
    private DataOutputStream[] toClients = new DataOutputStream[2];
    private DataInputStream[] fromClients = new DataInputStream[2];
    private AtomicInteger readyCount = new AtomicInteger(0);
    private int[][] grid;

    public ServerModel(int port, int[][] grid) throws IOException {
        this.grid = grid;
        serverSocket = new ServerSocket(port);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket[] getClients() {
        return clients;
    }

    public DataOutputStream[] getToClients() {
        return toClients;
    }

    public DataInputStream[] getFromClients() {
        return fromClients;
    }

    public AtomicInteger getReadyCount() {
        return readyCount;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void acceptClient(int index) throws IOException {
        clients[index] = serverSocket.accept();
        toClients[index] = new DataOutputStream(clients[index].getOutputStream());
        fromClients[index] = new DataInputStream(clients[index].getInputStream());
    }

    public void incrementReadyCount() {
        readyCount.incrementAndGet();
    }
}

