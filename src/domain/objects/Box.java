package domain.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import domain.models.RunningModeModel;

public class Box {
    private int x;
    private int y;
    private int width=45;
    private int height=55;
    private int barrierWidth = RunningModeModel.barrierWidth;
    private int barrierHeight = RunningModeModel.barrierHeight;
    //private int width=58;
    //private int height=72;
    private int boxSpeed=2;
    private BufferedImage image;
    public Box(int x,int y){
        this.x=x;
        this.y=y;
        try {
            image = ImageIO.read(getClass().getResource("/ui/images/box.png")); // Adjust image path
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(){
        y+=boxSpeed;
    }
    public void draw(Graphics g){
        g.drawImage(image, x - (width/2), y - (height/2), width, height, null);
    }
    public int getY(){
        return y;
    }
    
    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }
    
    public boolean repOk() {
        if (x < 0 || x > RunningModeModel.WIDTH || y < 0 || y > RunningModeModel.HEIGHT) {
            return false;
        }
        if (width <= 0 || height <= 0) {
            return false;
        }
        if (image == null) {
            return false;
        }
        return true;
    }
}
