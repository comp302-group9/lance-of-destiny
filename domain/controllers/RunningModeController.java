package domain.controllers;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import domain.models.RunningModeModel;
import ui.screens.RunningModeView;

public class RunningModeController implements KeyListener, Runnable {
    private RunningModeModel model;
    private RunningModeView view;
    private boolean[] keys;
    private long lastUpdateTime; 

    public RunningModeController(RunningModeModel model, RunningModeView view) {
        this.model = model;
        this.view = view;
        view.addKeyListener(this); // Add key listener to the view
        view.setFocusable(true); // Make panel focusable to receive key events
        keys = new boolean[256]; // Initialize the keys array
        lastUpdateTime = System.currentTimeMillis(); // Initialize last update time
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = System.currentTimeMillis();
            model.update(currentTime, keys, RunningModeView.WIDTH, RunningModeView.HEIGHT);
            view.repaint(); // Repaint the view
            try {
                Thread.sleep(10); // Pause for a short duration
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keys[keyCode] = true; // Mark the key as pressed
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keys[keyCode] = false; // Mark the key as released
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    // Other KeyListener methods (keyTyped) implementations
}
