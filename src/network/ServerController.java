package network;

import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import domain.controllers.RunningModeController;
import domain.models.RunningModeModel;
import ui.screens.RunningModeView;
import domain.models.BuildingModeModel;
import ui.screens.BuildingModeView;

public class ServerController {
    private ServerModel model;
    private ServerView view;
    private BuildingModeModel buildingModel;
    private BuildingModeView buildingView;

    public ServerController(ServerModel model, ServerView view, BuildingModeModel bModel, BuildingModeView bView) {
        this.model = model;
        this.view = view;
        this.buildingModel = bModel;
        this.buildingView = bView;

        view.addReadyButtonListener(e -> {
            model.setServerReady(true);
            view.setLeftPanelCenterText("Ready");
            model.notifyClient("SERVER_READY");
            checkIfAllReady();
        });
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutDownServer));

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
            sendGridToClient();
            startGame();
        }
    }

    private void sendGridToClient() {
        int[][] grid = buildingView.getGrid();
        model.getClientHandler().sendGrid(grid);
    }


    private void startGame() {
        // Your logic to start the game on the server side
        // Replace with actual game start logic
        PrintWriter serverOut = model.getClientHandler().getOut();
        BufferedReader serverIn = model.getClientHandler().getIn();
        
        RunningModeModel rmodel = new RunningModeModel(buildingModel.getUser(), buildingView.getGrid());
        RunningModeView rview = new RunningModeView(rmodel, true); // new constructor version for dual player mode
        RunningModeController rcontroller = new RunningModeController(rmodel, rview, buildingView.getGrid(), serverOut, serverIn, true);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(rview);
        frame.revalidate();
        frame.repaint();

        Thread gameThread = new Thread(rcontroller);
        gameThread.start();
    }

    public void addClientName(String clientName) {
        view.setRightPanelCenterText(clientName);
        view.setClientStatusLabelText("Not Ready");
    }

    public void setClientReadyStatus(String status) {
        view.setClientStatusLabelText(status);
    }

    private void shutDownServer() {
        model.getClientHandler().cleanUp();
        
    }
}