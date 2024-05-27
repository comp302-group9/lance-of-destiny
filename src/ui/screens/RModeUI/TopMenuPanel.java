package ui.screens.RModeUI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import domain.models.RunningModeModel;

public class TopMenuPanel extends JPanel {
    private JLabel chancesLabel;
    private JLabel livesLabel;
    private JLabel scoreLabel;
    private JButton pauseButton;
    private JButton quitButton;
    private JButton saveButton;
    private BufferedImage heartImage;
    private int livesCount;

    public TopMenuPanel(RunningModeModel model) {
        try {
            heartImage = ImageIO.read(getClass().getResource("/ui/images/Heart.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setOpaque(false);

        pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 18));
        pauseButton.setForeground(Color.BLACK);
        topLeftPanel.add(pauseButton);
/*/
        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 18));
        quitButton.setForeground(Color.BLACK);
        topLeftPanel.add(quitButton);

        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setForeground(Color.BLACK);
        topLeftPanel.add(saveButton);
        */

        add(topLeftPanel, BorderLayout.WEST);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);

        chancesLabel = new JLabel("Chances: " + model.getChances());
        chancesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        chancesLabel.setForeground(Color.WHITE);
        topRightPanel.add(chancesLabel);

        livesLabel = new JLabel();
        livesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        livesLabel.setForeground(Color.WHITE);
        topRightPanel.add(livesLabel);

        scoreLabel = new JLabel("Score: " + model.getScore());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        topRightPanel.add(scoreLabel);

        add(topRightPanel, BorderLayout.EAST);

        updateChances(model.getChances());
        updateScore(model.getScore());
    }

    public void addPauseButtonListener(ActionListener listener) {
        pauseButton.addActionListener(listener);
    }

    public void addQuitButtonListener(ActionListener listener) {
        quitButton.addActionListener(listener);
    }

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void updateChances(int chances) {
        chancesLabel.setText("Chances: " + chances);
        livesCount = chances;
        updateLivesLabel();
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    private void updateLivesLabel() {
        livesLabel.setText("");
        for (int i = 0; i < livesCount; i++) {
            livesLabel.setIcon(new ImageIcon(heartImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        }
    }
}
