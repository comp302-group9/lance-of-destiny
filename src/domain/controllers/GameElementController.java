package domain.controllers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import domain.models.GameSession;
import domain.models.RunningModeModel;
import ui.screens.MyGamesView;
import ui.screens.RunningModeView;
import ui.screens.BModeUI.GameElement;

public class GameElementController {
    private GameElement gameElement;
    private GameSession gameSession;
    private MyGamesView myGamesView;

    public GameElementController(GameElement gameElement, MyGamesView myGamesView) {
        this.gameElement = gameElement;
        this.gameSession = gameElement.getGameSession();
        this.myGamesView = myGamesView;
        setupListeners();
    }

    private void setupListeners() {
        gameElement.addPlayButtonListener(e -> playGame());
    }

    private void playGame() {
        // Create the running mode model and view based on the game session grid
    	//this.writeSomething("hi " + gameSession.getGrid());
    	
    	
    	// First, setup and display the new RunningModeView in a new JFrame
        JFrame newFrame = new JFrame("Running Mode - Game: " + gameSession.getGameId());
        RunningModeModel runningModel = new RunningModeModel(gameSession.getUser(), setGridToArray(), gameSession.getGameId());
        RunningModeView runningView = new RunningModeView(runningModel);
        RunningModeController runningController = new RunningModeController( runningModel, runningView);

        newFrame.setContentPane(runningView);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.pack();
        newFrame.setSize(600 * 16 / 9, 600); // Ensure aspect ratio is maintained
        newFrame.setLocationRelativeTo(null); // Center on screen
        newFrame.setVisible(true);

        // Start the game logic in a new thread
        Thread gameThread = new Thread(runningController);
        gameThread.start();

        // After setting up the new frame, dispose the old frame
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(myGamesView);
        if (frame != null) {
            frame.dispose();
        }
        
    }
    
    public int[][] setGridToArray() {
		String[] numbers = gameSession.getGrid().split(" ");
        int[][] gridArray = new int[11][19];  // 11 rows and 19 columns

        int index = 0;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 19; j++) {
                gridArray[i][j] = Integer.parseInt(numbers[index]);
                index++;
            }
        }
        return gridArray;
	}
    
    public void writeSomething(String s) {
    	System.out.println(s);
    }
}