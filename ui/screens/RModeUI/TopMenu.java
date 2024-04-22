package ui.screens.RModeUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TopMenu extends JPanel {
    private JLabel livesLabel;
    private JLabel highScoreLabel;
    private JButton pauseButton;
    private int lives = 3;
    BufferedImage heart;

    public TopMenu() {
        try {
            heart = ImageIO.read(getClass().getResource("/ui/images/Fireball.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        setLayout(null);
        setPreferredSize(new Dimension(900, 40));
        setBackground(new Color(0,0,0,0));

        livesLabel = new JLabel("Lives: <3 <3 <3");
        highScoreLabel = new JLabel("High Score: 0");
        pauseButton = new JButton("Pause");

        // Customize the appearance of labels and button if needed
        livesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pauseButton.setFont(new Font("Arial", Font.BOLD, 16));

        livesLabel.setBounds(10, 5, 150, 20);
        highScoreLabel.setBounds(160, 5, 150,20);

        // Add components to the panel
        add(livesLabel);
        add(highScoreLabel);
        add(pauseButton);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TopMenu panel = new TopMenu();
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}

