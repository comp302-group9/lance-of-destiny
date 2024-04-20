package domain.objects.Barrier;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import domain.models.RunningModeModel;

public abstract class Barrier {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected String name;
	protected String img;
	protected BufferedImage image;
	protected String message;

	// Constructor
	public Barrier() {
		try {
			this.image = ImageIO.read(getClass().getResource(this.getImg()));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public Barrier(int x, int y) {
		this.x=x;
		this.y=y;
		this.width= 33 * 900 / 512;
		this.height =  600 / 30;
		try {
			this.image = ImageIO.read(getClass().getResource(this.getImg()));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public String getImg() { 
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	// Method to handle the barrier being hit by the Fire Ball
	public abstract boolean onHit();

	// Getter methods
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = name;
	}

	public void draw(Graphics g) {
        // Draw the fireball
        if (image != null) {
            g.drawImage(image, x, y, RunningModeModel.barrierWidth,  RunningModeModel.barrierHeight, null);
        } 
    }
	public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); 
    }

	public String getMessage(){
		return message;
	}
}
