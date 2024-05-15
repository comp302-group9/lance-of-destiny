package network;

import network.ClientModel;
import network.ClientView;
import domain.controllers.RunningModeController;
import domain.models.RunningModeModel;
import ui.screens.RunningModeView;

import javax.swing.*;
import java.io.*;

public class ClientController {
    private ClientModel model;
    private ClientView view;

    public ClientController(ClientModel model, ClientView view) {
        this.model = model;
        this.view = view;
        view.addReadyButtonListener(e -> handleReadyButton());
        startListening();
    }

    private void handleReadyButton() {
        try {
            model.getToServer().writeUTF("READY");
            view.setReadyButtonEnabled(false);
            view.setStatusLabelText("Waiting for other player...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    String response = model.getFromServer().readUTF();
                    if ("ALL_READY".equals(response)) {
                        ObjectInputStream objectInputStream = new ObjectInputStream(model.getSocket().getInputStream());
                        int[][] receivedGrid = (int[][]) objectInputStream.readObject();
                        SwingUtilities.invokeLater(() -> {
                            view.setStatusLabelText("All players are ready! Starting...");
                            switchToRunningMode(receivedGrid);
                        });
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void switchToRunningMode(int[][] grid) {
        System.out.println("All players ready");

        RunningModeModel runningModeModel = new RunningModeModel();
        RunningModeView runningModeView = new RunningModeView(runningModeModel);
        RunningModeController runningModeController = new RunningModeController(runningModeModel, runningModeView, grid);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Running Mode");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(runningModeView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            runningModeView.requestFocusInWindow();
            Thread gameThread = new Thread(runningModeController);
            gameThread.start();
        });
    }
}

