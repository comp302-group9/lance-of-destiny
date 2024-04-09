package ui.screens;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierElement;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BuildingModeView extends JPanel{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private BufferedImage backgroundImage;
    private BuildingModeModel model;
    public BuildingModeView(BuildingModeModel model){
        this.model=model;
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
        int y = 50;

        for (int i = 0; i < 4; i++) {
			BarrierElement barrierElement = new BarrierElement();

			// Position the panel within the frame
			barrierElement.setBounds(550, y, 200, 100);

			// Add the BarrierElement JPanel to the main window
			this.add(barrierElement);

			y += 120; // Increase x-coordinate for the next panel, with a gap of 20 pixels
		}
    }
}
