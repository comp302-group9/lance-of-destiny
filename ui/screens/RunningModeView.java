package ui.screens;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.JPanel;

import domain.models.RunningModeModel;
import domain.objects.Fireball;
import domain.objects.Paddle;

public class RunningModeView extends JPanel{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private BufferedImage backgroundImage;
    private RunningModeModel model;

    public RunningModeView(RunningModeModel model){
        this.model=model;
        // Load the background image
        try {
            java.net.URL imageURL = getClass().getResource("/ui/images/Background.png");
            backgroundImage = ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the paddle
        Paddle paddle = model.getPaddle();
        paddle.draw(g);

        // Draw the fireball
        Fireball fireball = model.getFireball();
        fireball.draw(g);
    }
}
