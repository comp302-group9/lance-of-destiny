package domain.models;

import java.util.Random;

import javax.swing.ImageIcon;

public class YmirModel {
    private ImageIcon[] coinGifs;
    private ImageIcon[] witchGifs;
    private Random random;

    public YmirModel() {
        coinGifs = new ImageIcon[4];
        witchGifs = new ImageIcon[4];
        random = new Random();

        coinGifs[0] = new ImageIcon("src\\ui\\gifs\\coin.gif");
        coinGifs[1] = new ImageIcon("src\\ui\\gifs\\coinFast.gif");
        coinGifs[2] = new ImageIcon("src\\ui\\gifs\\coinHat.png");
        coinGifs[3] = new ImageIcon("src\\ui\\gifs\\coinWand.png");

        witchGifs[0] = new ImageIcon("src\\ui\\gifs\\witch-wait.gif");
        witchGifs[1] = new ImageIcon("src\\\\ui\\gifs\\witch-wand.gif");
        witchGifs[2] = new ImageIcon("src\\\\ui\\gifs\\witch-cast.gif");
        witchGifs[3] = new ImageIcon("src\\\\ui\\gifs\\witch-succ.gif");
    }

    public ImageIcon getWitchGif(int index) {
        return witchGifs[index];
    }

    public ImageIcon getCoinGif(int index) {
        return coinGifs[index];
    }

    public int getRandomOffset() {
        return random.nextInt(2);
    }
}
