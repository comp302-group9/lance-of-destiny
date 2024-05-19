package domain.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import domain.models.User;
import ui.screens.BuildingModeView;
import ui.screens.RunningModeView;

public class RunningModeController implements KeyListener, Runnable {
    private RunningModeModel model;
    private RunningModeView view;
    private User user;
    private boolean[] keys;
    private boolean isPaused = false;
    private boolean running = true;  // Flag to control the running of the game loop

    public RunningModeController(User user, RunningModeModel model, RunningModeView view, int[][] grid) {
        this.model = model;
        this.user = user;
        this.view = view;
        view.addKeyListener(this);
        view.setFocusable(true);
        keys = new boolean[256];  // Array to keep track of key states
        model.initaliseBarrierLocations(grid);
        setupQuitButtonListener();
    }
    
    private void setupQuitButtonListener() {
        view.addQuitButtonListener(e -> quitGame());
    }
    
    private void quitGame() {
        running = false; // Stop the game loop
        
        BuildingModeModel model2 = new BuildingModeModel(user);
        BuildingModeView view2 = new BuildingModeView(model2);
        BuildingModeController controller2 = new BuildingModeController(model2, view2);
        
        JFrame newFrame = new JFrame();
        newFrame.add(view2);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.pack();
        newFrame.setVisible(true);
        
        //frame.setSize(SignInPage.WIDTH, SignInPage.HEIGHT);
        newFrame.setLocationRelativeTo(null);
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
        if (frame != null) {
            frame.dispose(); // Close the current game window
            // Optionally, switch back to another view like the main menu
        }
        
    }
    
    @Override
    public void run() {
        while (true) {
            if (!model.isPaused()) {
                long currentTime = System.currentTimeMillis();
                model.update(currentTime, keys);
                view.repaint(); // Repaint the view
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