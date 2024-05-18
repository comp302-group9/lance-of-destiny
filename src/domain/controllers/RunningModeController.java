package domain.controllers;

import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import ui.screens.BuildingModeView;
import ui.screens.RunningModeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RunningModeController implements KeyListener, Runnable {
    private RunningModeModel model;
    private RunningModeView view;
    private boolean[] keys;
    private boolean running = true;

    public RunningModeController(RunningModeModel model, RunningModeView view, int[][] grid) {
        this.model = model;
        this.view = view;
        view.addKeyListener(this);
        view.setFocusable(true);
        keys = new boolean[256];
        model.initaliseBarrierLocations(grid);
        model.setGameOverCallback(this::handleGameOver);
    }

    private void handleGameOver() {
        // Display game over message for 5 seconds
        Timer timer = new Timer(5000, e -> switchToBuildingMode());
        timer.setRepeats(false);
        timer.start();
    }

    private void switchToBuildingMode() {
        SwingUtilities.invokeLater(() -> {
            BuildingModeModel buildingModel = new BuildingModeModel();
            BuildingModeView buildingView = new BuildingModeView(buildingModel);
            BuildingModeController buildingController = new BuildingModeController(buildingModel, buildingView);

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(buildingView);
            frame.revalidate();
            frame.repaint();
        });
    }

    @Override
    public void run() {
        while (running) {
            if (!model.isPaused() && !model.isGameOver()) {
                long currentTime = System.currentTimeMillis();
                model.update(currentTime, keys);
                view.repaint();

            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keys[keyCode] = true;

        if (keyCode == KeyEvent.VK_P) {
            model.setPaused(!model.isPaused());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed for this implementation
    }

    public void stopGame() {
        running = false;
    }

    
}
