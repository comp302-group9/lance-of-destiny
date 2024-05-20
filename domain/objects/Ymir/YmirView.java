package domain.objects.Ymir;

import javax.swing.*;
import java.awt.*;

public class YmirView extends JPanel {
    private JLabel gifLabel;
    private JLabel coinGifLabel;
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;

    public YmirView() {
        setLayout(null);

        gifLabel = new JLabel();
        coinGifLabel = new JLabel();

        gifLabel.setBounds(WIDTH - 127, HEIGHT - 90, 127, 90);
        gifLabel.setOpaque(false);

        coinGifLabel.setBounds(WIDTH - 117, HEIGHT - 80, 40, 40);
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

