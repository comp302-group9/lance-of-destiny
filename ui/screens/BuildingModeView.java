package ui.screens;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierElement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BuildingModeView extends JPanel{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private BufferedImage backgroundImage;
    private BuildingModeModel model;
    ImageIcon emptyIcon = new ImageIcon(getClass().getResource("/ui/images/Empty.png"));
    ImageIcon simpleIcon = new ImageIcon(getClass().getResource("/ui/images/simpleBarrierIcon.png"));
    ImageIcon firmIcon = new ImageIcon(getClass().getResource("/ui/images/reinforcedBarrierIcon.png"));
    ImageIcon redIcon = new ImageIcon(getClass().getResource("/ui/images/explosiveBarrierIcon.png"));
    ImageIcon greenIcon = new ImageIcon(getClass().getResource("/ui/images/rewardingBarrierIcon.png"));

    public BuildingModeView(BuildingModeModel model){
        this.model=model;
        try {
            java.net.URL imageURL = getClass().getResource("/ui/images/Background.png");
            backgroundImage = ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);

       addEmptyButtons();
       //addBarrierElements();
    }

    public void addEmptyButtons(){
        int buttonWidth = 7*HEIGHT/64;
        int buttonHeight = 2*WIDTH/72;
        int xStart = HEIGHT/32;
        int yStart = WIDTH/32;
        int xGap = HEIGHT/128;
        int yGap = WIDTH/96;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 11; col++) {
                JButton button = new JButton(emptyIcon);
                button.setFocusable(false);
                int x = xStart + col * (buttonWidth + xGap);
                int y = yStart + row * (buttonHeight + yGap);
                //if(row%2==0){x+=WIDTH/128;}
                button.setBounds(x, y, buttonWidth, buttonHeight);
                //button.setOpaque(false);
                button.setContentAreaFilled(false);
                //button.setBackground(new Color(0, 0, 0, 0));
                button.setBorderPainted(false);
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println("Button clicked at (" + button.getX() + ", " + button.getY() + ")");
                        switchBarrier(button);
                    }
                });
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setContentAreaFilled(true);
                        button.setBackground(Color.white);
                    }
        
                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setContentAreaFilled(false);
                    }
                });
                add(button);
            }
        }
    }

    public void switchBarrier(JButton button){
        if (button.getIcon()==emptyIcon){
            button.setIcon(simpleIcon);
        }
        else if (button.getIcon()==simpleIcon){
            button.setIcon(firmIcon);
        }
        else if (button.getIcon()==firmIcon){
            button.setIcon(redIcon);
        }
        else if (button.getIcon()==redIcon){
            button.setIcon(greenIcon);
        }
        else if (button.getIcon()==greenIcon){
            button.setIcon(emptyIcon);
        }
    }
    public void addBarrierElements(){
        int x=WIDTH/128;
        int y=3*HEIGHT/4;
        for (int i = 0; i < 4; i++) {
			BarrierElement barrierElement = new BarrierElement(null);

			// Position the panel within the frame
			barrierElement.setBounds(x, y, 200, 100);

			// Add the BarrierElement JPanel to the main window
			this.add(barrierElement);

			x += WIDTH/4; // Increase x-coordinate for the next panel, with a gap of 20 pixels
		}
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
