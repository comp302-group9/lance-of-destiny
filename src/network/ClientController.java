package network;

import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import domain.controllers.RunningModeController;
import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import ui.screens.BuildingModeView;
import ui.screens.RunningModeView;

public class ClientController {
    private ClientModel model;
    private ClientView view;
    private PrintWriter out;
    private Socket socket;
    private BuildingModeModel buildingModel;
    private BuildingModeView buildingView;

    public ClientController(ClientModel model, ClientView view , BuildingModeModel bModel, BuildingModeView bView) {
        this.model = model;
        this.view = view;
        this.buildingModel = bModel;
        this.buildingView = bView;

        view.addReadyButtonListener(e -> {
            model.setReady(true);
            sendReadySignal();
        });
        Runtime.getRuntime().addShutdownHook(new Thread(this::cleanUp));

        new Thread(this::startClient).start();
    }

    private void startClient() {
        try {
            socket = new Socket(model.getServerAddress(), 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                    receiveGrid(socket);
                    startGame();
                }
                view.setStatusLabelText(message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void startGame() {
        // Your logic to start the game on the server side
        // Replace with actual game start logic
        RunningModeModel rmodel = new RunningModeModel(buildingModel.getUser(), model.getGrid());
        RunningModeView rview = new RunningModeView(rmodel);
        RunningModeController rcontroller = new RunningModeController(rmodel, rview, model.getGrid());

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(rview);
        frame.revalidate();
        frame.repaint();

        Thread gameThread = new Thread(rcontroller);
        gameThread.start();
    }

    private void receiveGrid(Socket socket) {
        try {
            ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
            int[][] grid = (int[][]) objectIn.readObject();
            model.setGrid(grid);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    private void sendReadySignal() {
        out.println("READY");
    }

    private void cleanUp() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}