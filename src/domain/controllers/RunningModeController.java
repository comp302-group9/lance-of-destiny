package domain.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.SwingUtilities;
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
    private boolean countdownActive = false; // Flag to check if countdown is active
    private PrintWriter out;  // To send messages to the client or server
    private BufferedReader in;  // To receive messages from the client or server
    private boolean isDualPlayer = false;

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

    public RunningModeController(RunningModeModel model, RunningModeView view, int[][] grid, PrintWriter out, BufferedReader in, boolean isDualPlayer) {
        this.model = model;
        this.view = view;
        this.grid = grid;
        this.out = out;
        this.in = in;
        this.isDualPlayer = isDualPlayer;
        view.addKeyListener(this);
        view.setFocusable(true);
        keys = new boolean[256];  // Array to keep track of key states
        model.initializeGame();  // Reset or initialize game elements
        model.initaliseBarrierLocations(grid);
        model.getFireball().setGrid(grid);
        model.setGameOverCallback(this::handleGameOver);

        new Thread(this::receiveMessages).start();  // Start a thread to receive messages
    }


    private void handleGameOver() {
        // Display game over message for 5 seconds
        Timer timer = new Timer(5000, e -> view.quitGame());
        timer.setRepeats(false);
        timer.start();
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

    private void startCountdownAndLaunchFireball() {
        countdownActive = true;
        new Thread(() -> {
            try {
                for (int i = 3; i > 0; i--) {
                    final int count = i;
                    SwingUtilities.invokeLater(() -> view.setCountdownText(String.valueOf(count)));
                    out.println("COUNTDOWN_" + count);
                    Thread.sleep(1000);
                }
                SwingUtilities.invokeLater(() -> {
                    view.setCountdownText("");
                    model.getFireball().launch(model.getPaddle().getX() + model.getPaddle().getWidth() / 2, model.getPaddle().getY() - model.getFireball().getHeight());
                });

                out.println("LAUNCH_FIREBALL");  // Send launch fireball message to the client
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countdownActive = false;
            }
        }).start();
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("COUNTDOWN_")) {
                    int count = Integer.parseInt(message.split("_")[1]);
                    SwingUtilities.invokeLater(() -> view.setCountdownText(String.valueOf(count)));
                } else if (message.equals("LAUNCH_FIREBALL")) {
                    SwingUtilities.invokeLater(() -> {
                        view.setCountdownText("");
                        model.getFireball().launch(model.getPaddle().getX() + model.getPaddle().getWidth() / 2, model.getPaddle().getY() - model.getFireball().getHeight());
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

        // Start countdown and launch fireball when space key is pressed and fireball is not launched
        if (isDualPlayer && keyCode == KeyEvent.VK_SPACE && !model.getFireball().isLaunched() && !countdownActive) {
            out.println("START_COUNTDOWN");  // Send message to start countdown
            startCountdownAndLaunchFireball();
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