package ui.screens.BModeUI;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BarrierPlacementPanel extends JPanel{
    public static final int WIDTH = 580;
    public static final int HEIGHT = 435;
    int buttonWidth = 7*HEIGHT/64;
    int buttonHeight = 2*WIDTH/72;
    private BufferedImage backgroundImage;
    private ImageIcon empty = scaleImage("/ui/images/Empty3.png");
    private ImageIcon simple = scaleImage("/ui/images/simpleBarrierIcon.png");
    private ImageIcon firm = scaleImage("/ui/images/reinforcedBarrierIcon.png");
    private ImageIcon explosive = scaleImage("/ui/images/explosiveBarrierIcon.png");
    private ImageIcon rewarding = scaleImage("/ui/images/rewardingBarrierIcon.png");

    public BarrierPlacementPanel(){
        try {
            java.net.URL imageURL = getClass().getResource("/ui/images/Background.png");
            backgroundImage = ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);

       addEmptyButtons();
    }

    private ImageIcon scaleImage(String imagePath) {
        try {
            // Load the image
            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));

            // Scale the image to fit the button
            Image scaledImage = image.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addEmptyButtons(){

        int xStart = HEIGHT/32;
        int yStart = WIDTH/32;
        int xGap = HEIGHT/128;
        int yGap = WIDTH/96;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 11; col++) {
                JButton button = new JButton(empty);
                button.setFocusable(false);
                int x = xStart + col * (buttonWidth + xGap);
                int y = yStart + row * (buttonHeight + yGap);
                if(row%2==0){x+=WIDTH/128;}
                button.setBounds(x, y, buttonWidth, buttonHeight);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
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
        if (button.getIcon()==empty){
            button.setIcon(simple);
        }
        else if (button.getIcon()==simple){
            button.setIcon(firm);
        }
        else if (button.getIcon()==firm){
            button.setIcon(explosive);
        }
        else if (button.getIcon()==explosive){
            button.setIcon(rewarding);
        }
        else if (button.getIcon()==rewarding){
            button.setIcon(empty);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
