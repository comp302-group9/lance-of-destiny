package network;

import domain.controllers.RunningModeController;
import domain.models.RunningModeModel;
import ui.screens.RunningModeView;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ServerController {
    private ServerModel model;
    private ServerView view;

    public ServerController(ServerModel model, ServerView view) {
        this.model = model;
        this.view = view;
        view.addReadyButtonListener(e -> handleSelfReady());
    }

    public void execute() {
        try {
            for (int i = 0; i < model.getClients().length - 1; i++) {
                model.acceptClient(i);
                System.out.println("Player " + (i + 1) + " connected.");
                final int playerIndex = i;
                new Thread(() -> handleClient(playerIndex)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSelfReady() {
        view.setReadyButtonEnabled(false);
        view.setStatusLabelText("Server Ready");
        model.incrementReadyCount();
        if (model.getReadyCount().get() == model.getClients().length) {
            notifyAllPlayersReady();
        }
    }

    private void notifyAllPlayersReady() {
        System.out.println("All players are ready. Starting game...");
        for (int i = 0; i < model.getToClients().length - 1; i++) {
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(model.getToClients()[i]);
                objectOutputStream.writeObject(model.getGrid());
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        switchToRunningMode(model.getGrid());
    }

    private void handleClient(int playerIndex) {
        try {
            while (true) {
                String message = model.getFromClients()[playerIndex].readUTF();
                if ("READY".equals(message)) {
                    System.out.println("Player " + (playerIndex + 1) + " is ready.");
                    model.incrementReadyCount();
                    if (model.getReadyCount().get() == model.getClients().length) {
                        notifyAllPlayersReady();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToRunningMode(int[][] grid) {
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

    public void actLikeClient(String serverAddress, int serverPort) {
        try {
            int lastIndex = model.getClients().length - 1;
            model.getClients()[lastIndex] = new Socket(serverAddress, serverPort);
            model.getToClients()[lastIndex] = new DataOutputStream(model.getClients()[lastIndex].getOutputStream());
            model.getFromClients()[lastIndex] = new DataInputStream(model.getClients()[lastIndex].getInputStream());
            handleClient(lastIndex);
            view.addReadyButtonListener(e -> handleSelfReady());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

