package ui.screens;

import javax.swing.*;
import java.awt.*;

public class GameOverView extends JPanel {

    public GameOverView(String userName, int score) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel gameOverLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(gameOverLabel, gbc);

        JLabel userNameLabel = new JLabel("Player: " + userName, SwingConstants.CENTER);
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 1;
        add(userNameLabel, gbc);

        JLabel scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 2;
        add(scoreLabel, gbc);
    }
}
