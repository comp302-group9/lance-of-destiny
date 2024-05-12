package domain.controllers;

import java.util.ArrayList;

import javax.swing.JFrame;

import domain.models.BuildingModeModel;
import domain.models.GameSession;
import ui.screens.BuildingModeView;
import ui.screens.MyGamesView;

public class MyGamesController {

	private MyGamesView myGamesView;
	private GameSession g;
	private ArrayList<GameSession> games = new ArrayList<>();

	public MyGamesController(MyGamesView myGamesView, GameSession g) {
		this.myGamesView = myGamesView;
		this.g = g;
		setupListeners();
	}
	
	private void setupListeners() {
        myGamesView.addBackButtonListener(e -> returnToBuildingMode());
    }
	
	private void returnToBuildingMode() {
//        SignInView signInView = new SignInView();
//        SignInController controller = new SignInController(signInView);
//        signInView.display();
//        signUpView.closeFrame();
        
        BuildingModeModel model2 = new BuildingModeModel(g.getUser());
        BuildingModeView view2 = new BuildingModeView(model2);
        BuildingModeController controller2 = new BuildingModeController(model2, view2);
        
        JFrame frame = new JFrame();
        frame.add(view2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        //frame.setSize(SignInPage.WIDTH, SignInPage.HEIGHT);
        frame.setLocationRelativeTo(null);
        
        myGamesView.closeFrame();
    }
}
