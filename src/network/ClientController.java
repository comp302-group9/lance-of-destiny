package network;

import java.awt.Color;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domain.controllers.RunningModeController;
import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import ui.screens.BuildingModeView;
import ui.screens.RunningModeView;
import ui.screens.RModeUI.GameStatusPanel;

public class ClientController implements Connectable{
    private static Connectable instance;
    private ClientModel model;
    private ClientView view;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private BuildingModeModel buildingModel;
    private BuildingModeView buildingView;
    private GameStatusPanel panel;

    private ClientController(ClientModel model, ClientView view, BuildingModeModel bModel, BuildingModeView bView) {
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

    public static synchronized Connectable getInstance(ClientModel model, ClientView view, BuildingModeModel bModel, BuildingModeView bView) {
        if (instance == null) {
            instance = new ClientController(model, view, bModel, bView);
        }
        return instance;
    }

    private void startClient() {
        try {
            socket = new Socket(model.getServerAddress(), 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(model.getClientName());

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                System.out.println(message);
                if (message.equals("SERVER_READY")) {
                    view.setServerStatusLabelText("Ready");
                } else if (message.equals("All players are ready!")) {
                    view.setAllPlayersReadyText(message);
                    receiveGrid(socket);
                    startGame();
                }else if (message.startsWith("S")) {
                    System.out.println("second "+message);
                    String numberPart = message.substring(1); // Extract the part of the message after "S"
                    try {
                        int number = Integer.parseInt(numberPart); // Convert the extracted part to an integer
                        System.out.println("Score: "+number);
                        System.out.println(panel);
                        panel.setScore(number);

                    } catch (NumberFormatException e) {}
                }
                //Mesajlar buradan alınıyor Lives ve barrier number eklenecek
                //Task 1
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void startGame() {
        RunningModeModel rmodel = new RunningModeModel(buildingModel.getUser(), model.getGrid(), in, out, false);
        RunningModeView rview = new RunningModeView(rmodel, true, this);
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

    public void sendScore(int score){
        out.println("S"+score);
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

    @Override
    public void sendBarriers(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendBarriers'");
    }

    @Override
    public void sendLives(int i) {
        // Task 1
        //Mesajlar buradan gönderiliyor
    }

    @Override
    public Connectable getInstance() {
        System.out.println(instance);
        // TODO Auto-generated method stub
        if(instance!=null)return instance;
        return null;

    }

    @Override
    public void setGSP(GameStatusPanel g) {
        // TODO Auto-generated method stub
        this.panel=g;
    }
}
