package domain.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import domain.models.RunningModeModel;
import domain.objects.Spells.Spell;

public class Box implements GameObject{
    private int x;
    private int y;
    private int width=45;
    private int height=55;
    //private int width=58;
    //private int height=72;
    private int boxSpeed=2;
    private BufferedImage image;
    private Spell spell;
    public Box(int x,int y){
        this.x=x;
        this.y=y;
        try {image = ImageIO.read(getClass().getResource("/ui/images/box.png"));} // Adjust image path} 
        catch (IOException e) {e.printStackTrace();}
    } 

    public void move(){
        y+=boxSpeed;
    }

    public void draw(Graphics g){
        g.drawImage(image, x - (width/2), y - (height/2), width, height, null);
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
    
    public boolean hasSpell() {
        return spell != null;
    }
    
    public void openBox() {
        if (spell != null) {
            spell.Activate();
        }
    }
    
    @Override
    public Shape getBounds() {
        return new Rectangle(x - (width / 2), y - (height / 2), width, height);
    }

    public int getY(){return y;}
    public int getX() {return x;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public BufferedImage getImage() {return image;}
}