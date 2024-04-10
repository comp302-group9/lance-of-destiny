package ui.screens;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierElement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BuildingModeView extends JPanel{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private BufferedImage backgroundImage;
    private BufferedImage emptyImage;
    private BuildingModeModel model;
    public BuildingModeView(BuildingModeModel model){
        this.model=model;
        try {
            java.net.URL imageURL = getClass().getResource("/ui/images/Background.png");
            backgroundImage = ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon emptyIcon = new ImageIcon(getClass().getResource("/ui/images/Empty.png"));
        
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);

        int buttonWidth = 7*HEIGHT/64;
        int buttonHeight = 2*WIDTH/72;
        int xStart = HEIGHT/32;
        int yStart = WIDTH/32;
        int xGap = HEIGHT/128;
        int yGap = WIDTH/96;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 11; col++) {
                JButton button = new JButton(emptyIcon);
                int x = xStart + col * (buttonWidth + xGap);
                int y = yStart + row * (buttonHeight + yGap);
                if(row%2==0){x+=WIDTH/128;}
                button.setBounds(x, y, buttonWidth, buttonHeight);
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Button clicked at (" + button.getWidth() + ", " + button.getHeight() + ")");
                    }
                });
                add(button);
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        int y = 50;
/*/
        for (int i = 0; i < 4; i++) {
			BarrierElement barrierElement = new BarrierElement();

			// Position the panel within the frame
			barrierElement.setBounds(550, y, 200, 100);

			// Add the BarrierElement JPanel to the main window
			this.add(barrierElement);

			y += 120; // Increase x-coordinate for the next panel, with a gap of 20 pixels
		}*/
    }
}
