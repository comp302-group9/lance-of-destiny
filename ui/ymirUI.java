package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ymirUI {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;
    static ImageIcon[] coinGifs = new ImageIcon[4];
    static ImageIcon[] witchGifs = new ImageIcon[4];
    static int currentGifIndex = 0;
    static Random random = new Random();
    static Timer timer;
    public static void addYmir(JPanel panel){
        
        coinGifs[0] = new ImageIcon("ui\\gifs\\coin.gif");
        coinGifs[1] = new ImageIcon("ui\\gifs\\coinFast.gif");
        coinGifs[2] = new ImageIcon("ui\\gifs\\coinHat.png");
        coinGifs[3] = new ImageIcon("ui\\gifs\\coinWand.png");

        //ImageIcon firstGifIcon = new ImageIcon("ui\\gifs\\TwoFaceGif-small.gif");
        witchGifs[0] = new ImageIcon("ui\\gifs\\witch-wait.gif");
        witchGifs[1] = new ImageIcon("ui\\gifs\\witch-wand.gif");
        witchGifs[2] = new ImageIcon("ui\\gifs\\witch-cast.gif");
        witchGifs[3] = new ImageIcon("ui\\gifs\\witch-succ.gif");
        JLabel gifLabel = new JLabel(witchGifs[0]);
        JLabel coinGifLabel = new JLabel(coinGifs[0]);

        int x = 2500;
        timer = new Timer(x, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentGifIndex = (currentGifIndex + 1) % 3; // Increment current index cyclically
            if (currentGifIndex == 2) {
                
                int rand= random.nextInt(2);
                gifLabel.setIcon(witchGifs[currentGifIndex + rand]); // Set the icon to the current GIF
                coinGifLabel.setIcon(coinGifs[currentGifIndex + rand]);
            }else{
                gifLabel.setIcon(witchGifs[currentGifIndex]); // Set the icon to the current GIF
                coinGifLabel.setIcon(coinGifs[currentGifIndex]);
            }
            //24900
            timer.setDelay(currentGifIndex == 0 ? 2450 : (currentGifIndex == 1 ? 2600 : x));
            }
        });
        timer.setDelay(currentGifIndex == 0 ? 2400 : (currentGifIndex == 1 ? 2600 : x));
        timer.start();

        
        gifLabel.setBounds(WIDTH-127, HEIGHT-90, 127, 90);
        gifLabel.setOpaque(false);
        //Border border = BorderFactory.createLineBorder(Color.BLACK); // Create a black border
        //gifLabel.setBorder(border);
        panel.add(gifLabel);

        coinGifLabel.setBounds(WIDTH-117, HEIGHT-80, 40, 40);
        coinGifLabel.setOpaque(false);
        panel.add(coinGifLabel);

    }
}
