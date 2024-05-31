package network;

import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import domain.controllers.RunningModeController;
import domain.models.RunningModeModel;
import ui.screens.RunningModeView;
import ui.screens.RModeUI.GameStatusPanel;
import domain.models.BuildingModeModel;
import ui.screens.BuildingModeView;

public class ServerController implements Connectable{
    private static ServerController instance;
    private ServerModel model;
    private ServerView view;
    private BuildingModeModel buildingModel;
    private BuildingModeView buildingView;
    private ClientHandler clientHandler;

    private ServerController(ServerModel model, ServerView view, BuildingModeModel bModel, BuildingModeView bView) {
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

    public static synchronized ServerController getInstance(ServerModel model, ServerView view, BuildingModeModel bModel, BuildingModeView bView) {
        if (instance == null) {
            instance = new ServerController(model, view, bModel, bView);
        }
        return instance;
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            String serverIp = InetAddress.getLocalHost().getHostAddress();
            view.setIpLabelText(serverIp);

            while (true) {
                Socket socket = serverSocket.accept();
                clientHandler = new ClientHandler(socket, this);
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
        PrintWriter serverOut = model.getClientHandler().getOut();
        BufferedReader serverIn = model.getClientHandler().getIn();

        RunningModeModel rmodel = new RunningModeModel(buildingModel.getUser(), buildingView.getGrid(), serverIn, serverOut, true);
        RunningModeView rview = new RunningModeView(rmodel, true, this);
        RunningModeController rcontroller = new RunningModeController(rmodel, rview, buildingView.getGrid());

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

    @Override
    public Connectable getInstance() {
        // TODO Auto-generated method stub
        return instance;
    }

    @Override
    public void sendScore(int i) {
        // TODO Auto-generated method stub
        model.notifyClient("S"+i);
        
    }

    @Override
    public void sendBarriers(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendBarriers'");
    }

    @Override
    public void sendLives(int i) {
        //Mesaj buradan göneriliyor
        //Task 1
    }

    public void setGSP(GameStatusPanel g){
        this.clientHandler.setGSP(g);
    }
}
