package domain.controllers;

import javax.swing.JFrame;

import domain.models.GameSession;
import domain.models.RunningModeModel;
import ui.screens.RunningModeView;
import ui.screens.BModeUI.GameElement;

public class GameElementController {
    private GameElement gameElement;
    private GameSession gameSession;

    public GameElementController(GameElement gameElement) {
        this.gameElement = gameElement;
        this.gameSession = gameElement.getGameSession();
        setupListeners();
    }

    private void setupListeners() {
        gameElement.addPlayButtonListener(e -> playGame());
    }

    private void playGame() {
        // Create the running mode model and view based on the game session grid
    	this.writeSomething("hi " + gameSession.getGrid());
    	
    	
        RunningModeModel runningModel = new RunningModeModel();
        RunningModeView runningView = new RunningModeView(runningModel);
        RunningModeController runningController = new RunningModeController(runningModel, runningView, setGridToArray());

        // Set up the frame for the running mode view
        JFrame frame = new JFrame("Running Mode - Game: " + gameSession.getGameId());
        frame.setContentPane(runningView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        
        
        //BURADAKI UI 'IN DEĞİŞTİRİLMESİ GEREKİYOR ÇOK KİRİTK BAŞIMIZ AĞRIR
        frame.setSize(1067, 600);
        frame.setVisible(true);

        // Start the game logic in a new thread
        Thread gameThread = new Thread(runningController);
        gameThread.start();
        
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
