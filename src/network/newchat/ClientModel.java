package network.newchat;

// ClientModel.java
public class ClientModel {
    private String serverAddress;
    private String clientName;
    private boolean ready;

    public ClientModel(String serverAddress, String clientName) {
        this.serverAddress = serverAddress;
        this.clientName = clientName;
        this.ready = false;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}