package domain.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import database.DatabaseConnection;
import domain.models.BuildingModeModel;
import domain.models.GameSession;
import domain.models.User;
import ui.screens.BuildingModeView;
import ui.screens.MyGamesView;
import ui.screens.BModeUI.GameElement;

public class MyGamesController {

	private MyGamesView myGamesView;
	private User user;
	private ArrayList<GameSession> games = new ArrayList<>();

	public MyGamesController(MyGamesView myGamesView, User user) {
		this.myGamesView = myGamesView; 
		this.user = user;
		setupListeners();
		fetchSavedGamesForUser();
	}
	
	private void setupListeners() {
        myGamesView.addBackButtonListener(e -> returnToBuildingMode());
    }
	
	private void fetchSavedGamesForUser() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT gameId, username, life, score, grid FROM SavedGames WHERE username = ?")) {
            pstmt.setString(1, this.user.getUsername());  // Assuming User has an getId() method

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int gameId = rs.getInt("gameId");
                String username = rs.getString("username");
                int life = rs.getInt("life");
                int score = rs.getInt("score");
                String grid = rs.getString("grid");

                GameSession session = new GameSession(new User(this.user.getUsername(), this.user.getPassword(), this.user.getEmail()), gameId, life, score, grid);
                
                games.add(session);
                
                // Inform the view about this game session
                GameElement gameElement = new GameElement(session);
                new GameElementController(gameElement, myGamesView);
                myGamesView.addGameElement(gameElement, games.size());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	private void returnToBuildingMode() {
//        SignInView signInView = new SignInView();
//        SignInController controller = new SignInController(signInView);
//        signInView.display();
//        signUpView.closeFrame();
        
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
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(myGamesView);
        if (frame != null) {
            frame.dispose(); // Close the current game window
            // Optionally, switch back to another view like the main menu
        }
    }
}