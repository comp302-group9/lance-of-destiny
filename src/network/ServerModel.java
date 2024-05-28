package network;

// ServerModel.java
public class ServerModel {
    private ClientHandler clientHandler;
    private boolean serverReady;

    public ServerModel() {
        serverReady = false;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public boolean isServerReady() {
        return serverReady;
    }

    public void setServerReady(boolean serverReady) {
        this.serverReady = serverReady;
    }

    public boolean isClientReady() {
        return clientHandler != null && clientHandler.isReady();
    }

    public void notifyClient(String message) {
        if (clientHandler != null) {
            clientHandler.sendMessage(message);
        }
    }
}