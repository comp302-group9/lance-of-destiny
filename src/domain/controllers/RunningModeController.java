package domain.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import domain.models.RunningModeModel;
import ui.screens.GameOverView;
import ui.screens.GameWinView;
import ui.screens.RunningModeView;


public class RunningModeController implements KeyListener, Runnable {
    private RunningModeModel model;
    private RunningModeView view;
    private boolean[] keys;
    private int[][] grid;
    private boolean isPaused = false;
    private boolean running = true;  // Flag to control the running of the game loop
    private boolean countdownActive = false; // Flag to check if countdown is active
    private PrintWriter out;  // To send messages to the client or server
    private BufferedReader in;  // To receive messages from the client or server
    private boolean isHost = false;

    public RunningModeController(RunningModeModel model, RunningModeView view, int[][] grid) {
        this.model = model;
        this.view = view;
        this.grid = grid;
        view.addKeyListener(this);
        view.setFocusable(true);
        keys = new boolean[256];  // Array to keep track of key states
        //model.initializeGame();  // Reset or initialize game elements
        model.initaliseBarrierLocations(grid);
        //model.setFireball(ObjectFactory.getInstance().createFireball(0, 0, 16, 16));
        model.getFireball().setGrid(grid);
        model.setGameOverCallback(this::handleGameOver);
        model.setGameWinCallback(this::handleGameWin);
    }
    /* 
    public RunningModeController(RunningModeModel model, RunningModeView view, int[][] grid, PrintWriter out, BufferedReader in, boolean isHost) {
        this.model = model;
        this.view = view;
        this.grid = grid;
        this.out = out;
        this.in = in;
        this.isHost = isHost;
        view.addKeyListener(this);
        view.setFocusable(true);
        keys = new boolean[256];  // Array to keep track of key states
        //model.initializeGame();  // Reset or initialize game elements
        model.initaliseBarrierLocations(grid);
        model.getFireball().setGrid(grid);
        model.setGameOverCallback(this::handleGameOver);

        //new Thread(this::receiveMessages).start();  // Start a thread to receive messages
    } */

    private void handleGameOver() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            frame.setContentPane(new GameOverView(model.getUser().getUsername(), model.getScore()));
            frame.revalidate();
        });
        stopGame();
    }

    private void handleGameWin() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            frame.setContentPane(new GameWinView(model.getUser().getUsername(), model.getChances(), model.getScore()));
            frame.revalidate();
        });
        stopGame();
    }

    

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
        
        // Launch the fireball when space is pressed
        if (keyCode == KeyEvent.VK_SPACE && !model.getFireball().isLaunched()) {
            model.getFireball().launch(model.getPaddle().getX() + model.getPaddle().getWidth() / 2, model.getPaddle().getY() - model.getFireball().getHeight());
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