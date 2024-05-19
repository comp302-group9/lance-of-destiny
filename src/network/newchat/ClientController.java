package network.newchat;

import java.io.*;
import java.net.*;

public class ClientController {
    private ClientModel model;
    private ClientView view;
    private PrintWriter out;

    public ClientController(ClientModel model, ClientView view) {
        this.model = model;
        this.view = view;

        view.addReadyButtonListener(e -> {
            model.setReady(true);
            sendReadySignal();
        });

        new Thread(this::startClient).start();
    }

    private void startClient() {
        try (Socket socket = new Socket(model.getServerAddress(), 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(model.getClientName());

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                if (message.equals("SERVER_READY")) {
                    view.setServerStatusLabelText("Ready");
                } else if (message.equals("All players are ready!")) {
                    view.setAllPlayersReadyText(message);
                }
                view.setStatusLabelText(message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendReadySignal() {
        out.println("READY");
    }
}
