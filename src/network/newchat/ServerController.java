package network.newchat;

import java.io.*;
import java.net.*;

public class ServerController {
    private ServerModel model;
    private ServerView view;

    public ServerController(ServerModel model, ServerView view) {
        this.model = model;
        this.view = view;

        view.addReadyButtonListener(e -> {
            model.setServerReady(true);
            view.setLeftPanelCenterText("Ready");
            model.notifyClient("SERVER_READY");
            checkIfAllReady();
        });

        new Thread(this::startServer).start();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            String serverIp = InetAddress.getLocalHost().getHostAddress();
            view.setIpLabelText(serverIp);
            System.out.println("Server is listening on port 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(socket, this);
                model.setClientHandler(clientHandler);
                new Thread(clientHandler).start();
                break;  // Only allow one client to connect
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void checkIfAllReady() {
        if (model.isServerReady() && model.isClientReady()) {
            view.setStatusLabelText("All players are ready!");
            view.setAllPlayersReadyText("All players are ready!");
            model.notifyClient("All players are ready!");
        }
    }

    public void addClientName(String clientName) {
        view.setRightPanelCenterText(clientName);
        view.setClientStatusLabelText("Not Ready");
    }

    public void setClientReadyStatus(String status) {
        view.setClientStatusLabelText(status);
    }
}
