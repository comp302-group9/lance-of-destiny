package ui.screens.RModeUI;

import javax.swing.*;
import java.awt.*;

public class YmirView extends JPanel {
    private JLabel gifLabel;
    private JLabel coinGifLabel;
    private static final int WIDTH = 127;
    private static final int HEIGHT = 90;

    public YmirView() {
        setLayout(null);
        setOpaque(false);
        gifLabel = new JLabel();
        coinGifLabel = new JLabel();

        gifLabel.setBounds(0, 0, 127, 90);
        gifLabel.setOpaque(false);

        coinGifLabel.setBounds(0, 10, 40, 40);
        coinGifLabel.setOpaque(false);

        add(gifLabel);
        add(coinGifLabel);
    }

    public void updateWitchGif(ImageIcon gif) {
        gifLabel.setIcon(gif);
    }

    public void updateCoinGif(ImageIcon gif) {
        coinGifLabel.setIcon(gif);
    }
}

