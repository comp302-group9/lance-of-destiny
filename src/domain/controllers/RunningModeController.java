package domain.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import database.DatabaseConnection;
import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import domain.models.User;
import ui.screens.BuildingModeView;
import ui.screens.RunningModeView;

public class RunningModeController implements KeyListener, Runnable {
    private RunningModeModel model;
    private RunningModeView view;
    private User user;
    private int gameId;
    private boolean[] keys;
    private boolean isPaused = false;
    private int[][] grid;
    private boolean running = true;  // Flag to control the running of the game loop
    

    public RunningModeController(User user, RunningModeModel model, RunningModeView view, int[][] grid) {
    	this.grid = grid;
        this.model = model;
        this.user = user;
        this.view = view;
        view.addKeyListener(this);
        view.setFocusable(true);
        keys = new boolean[256];  // Array to keep track of key states
        model.initaliseBarrierLocations(grid);
        model.getFireball().setGrid(grid);;
        setupQuitButtonListener();
        setupSaveButtonListener();
        model.setGameOverCallback(this::handleGameOver);
    }
    
    
    public RunningModeController(User user, RunningModeModel model, RunningModeView view, int[][] grid, int gameId) {
    	this.grid = grid;
    	this.gameId = gameId;
        this.model = model;
        this.user = user;
        this.view = view;
        view.addKeyListener(this);
        view.setFocusable(true);
        keys = new boolean[256];  // Array to keep track of key states
        model.initaliseBarrierLocations(grid);
        model.getFireball().setGrid(grid);;
        setupQuitButtonListener();
        setupSaveButtonListener();
        model.setGameOverCallback(this::handleGameOver);
    }
    
    private void handleGameOver() {
        // Display game over message for 5 seconds
        Timer timer = new Timer(5000, e -> quitGame());
        timer.setRepeats(false);
        timer.start();
    }
    
//    private void switchToBuildingMode() {
//        SwingUtilities.invokeLater(() -> {
//            BuildingModeModel buildingModel = new BuildingModeModel(user);
//            BuildingModeView buildingView = new BuildingModeView(buildingModel);
//            BuildingModeController buildingController = new BuildingModeController(buildingModel, buildingView);
//
//            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
//            frame.getContentPane().removeAll();
//            frame.getContentPane().add(buildingView);
//            frame.revalidate();
//            frame.repaint();
//        });
//    }
    
    
    
    
    private void setupQuitButtonListener() {
        view.addQuitButtonListener(e -> quitGame());
    }
    
    
    private void setupSaveButtonListener() {
    	view.addSaveButtonListener(e -> saveGame(grid, gameId));
    }
    
    
//    private void saveGame() {
//        model.saveGame();
//    }
    
    public void saveGame(int[][] matrix, int gameId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE SavedGames SET grid = ? WHERE gameId = ?";
            pstmt = conn.prepareStatement(sql);

//            // Convert the 2D array into a single string
//            StringBuilder gridBuilder = new StringBuilder();
//            for (int i = 0; i < matrix.length; i++) {
//                for (int j = 0; j < matrix[i].length; j++) {
//                    gridBuilder.append(matrix[i][j]).append(" ");
//                }
//            }
//            String gridString = gridBuilder.toString().trim(); // Remove trailing space
            
            String gridString = writeGrid(matrix);

            pstmt.setString(1, gridString);
            pstmt.setInt(2, gameId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating the grid failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
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
        while (running) {
            if (!model.isPaused() && !model.isGameOver()) {
                long currentTime = System.currentTimeMillis();
                model.update(currentTime, keys);
                //System.out.println(writeGrid(model.getGrid()));
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
    
    
    public String writeGrid(int[][] matrix) {
	    StringBuilder gridStringBuilder = new StringBuilder();
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix[i].length; j++) {
	            gridStringBuilder.append(matrix[i][j]).append(" ");
	        }
	    }
	    String gridString = gridStringBuilder.toString().trim(); // Remove trailing space
	    return gridString;
	    
	}
}