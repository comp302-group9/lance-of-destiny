package ui.screens;

import javax.swing.*;
import java.awt.*;

public class GameWinView extends JPanel {

    public GameWinView(String userName, int chances, int score) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel gameWinLabel = new JLabel("You Win!", SwingConstants.CENTER);
        gameWinLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameWinLabel.setForeground(Color.GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(gameWinLabel, gbc);

        JLabel userNameLabel = new JLabel("Player: " + userName, SwingConstants.CENTER);
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 1;
        add(userNameLabel, gbc);

        JLabel chancesLabel = new JLabel("Chances: " + chances, SwingConstants.CENTER);
        chancesLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 2;
        add(chancesLabel, gbc);

        JLabel scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 3;
        add(scoreLabel, gbc);
    }
}
