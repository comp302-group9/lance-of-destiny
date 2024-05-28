package domain.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import domain.models.RunningModeModel;
import ui.screens.RunningModeView;


public class RunningModeController implements KeyListener, Runnable {
    private RunningModeModel model;
    private RunningModeView view;
    private boolean[] keys;
    private int[][] grid;
    private boolean isPaused = false;
    private boolean running = true;  // Flag to control the running of the game loop

    public RunningModeController(RunningModeModel model, RunningModeView view, int[][] grid) {
        this.model = model;
        this.view = view;
        this.grid = grid;
        view.addKeyListener(this);
        view.setFocusable(true);
        keys = new boolean[256];  // Array to keep track of key states
        
        
        model.initializeGame();  // Reset or initialize game elements
        model.initaliseBarrierLocations(grid);
        model.getFireball().setGrid(grid);
        
        //setupQuitButtonListener();
        //setupSaveButtonListener();
        model.setGameOverCallback(this::handleGameOver);
       
    }


    private void handleGameOver() {
        // Display game over message for 5 seconds
        Timer timer = new Timer(5000, e -> view.quitGame());
        timer.setRepeats(false);
        timer.start();
    }

/* 
    private void setupQuitButtonListener() {
        view.addQuitButtonListener(e -> quitGame());
    }

    private void setupSaveButtonListener() {
    	view.addSaveButtonListener(e -> saveGame(grid, gameId));
    */

    @Override
    public void run() {
        while (running) {
            if (!model.isPaused() && !model.isGameOver()) {
                long currentTime = System.currentTimeMillis();
                model.update(currentTime, keys);
                
                
                
                view.repaint(); // Repaint the view
                view.updateChances();
            }
        
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
    
        // Toggle pause state when 'P' is pressed
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
        running = false;  // Method to stop the game loop
    }
}