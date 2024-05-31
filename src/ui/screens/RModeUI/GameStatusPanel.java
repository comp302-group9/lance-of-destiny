package ui.screens.RModeUI;

import javax.swing.*;
import java.awt.*;

public class GameStatusPanel extends JPanel {
    private JLabel scoreLabel;
    private JLabel livesLabel;
    private JLabel barriersLeftLabel;

    public GameStatusPanel() {
        // Initialize the labels
        scoreLabel = new JLabel("Score: 100");
        livesLabel = new JLabel("Lives: 3");
        barriersLeftLabel = new JLabel("Barriers Left: 5");

        // Set the layout of the panel
        setLayout(new GridLayout(1, 3));

        // Add the labels to the panel
        add(scoreLabel);
        add(livesLabel);
        add(barriersLeftLabel);
    }

    // Method to update the score
    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    // Method to update the lives
    public void setLives(int lives) {
        livesLabel.setText("Lives: " + lives);
    }

    // Method to update the barriers left
    public void setBarriersLeft(int barriersLeft) {
        barriersLeftLabel.setText("Barriers Left: " + barriersLeft);
    }
}
