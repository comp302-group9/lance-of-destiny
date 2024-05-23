package ui.screens;

import javax.swing.*;
import java.awt.*;

public class FinalScoreView extends JPanel {
    private int finalScore;

    public FinalScoreView(int finalScore) {
        this.finalScore = finalScore;
        setPreferredSize(new Dimension(800, 600)); // Set your desired dimensions
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.BLACK);
        g.drawString("Game Over!", 300, 200);
        g.drawString("Final Score: " + finalScore, 300, 300);
    }
}
