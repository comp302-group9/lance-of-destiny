package domain.objects.Barrier;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
	protected boolean isMoving;
    protected int direction; // 0 for left, 1 for right (for horizontal movement)


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
		this.isMoving = checkIfMoving();
        if (isMoving) {
            this.direction = new Random().nextBoolean() ? 0 : 1; // Random initial direction
        }
		try {
			this.image = ImageIO.read(getClass().getResource(this.getImg()));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public boolean checkIfMoving() {
        // Check if there is free space around the barrier in the x-axis
        // If there is free space, return true with a probability of 0.2
        return (new Random().nextDouble() < 0.2);
    }
	public boolean collidesWithBarrier(ArrayList<Barrier> barriers) {
        Rectangle bounds = getBounds();
        for (Barrier barrier : barriers) {
            if (barrier != this && barrier.getBounds().intersects(bounds)) {//.equals gerekebilir
                return true; // Collision detected
            }
        }
        return false; // No collision
    }
	
	public String getImg() { 
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	// Method to handle the barrier being hit by the Fire Ball
	public abstract boolean onHit();
	public abstract void move();
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
	 public Rectangle getFutureBounds() {
	        // Calculate the future x-coordinate based on the current direction
	        int futureX = x;
	        if (isMoving) {
	            if (direction == 0) {
	                futureX -= L / 4; // Move left
	            } else {
	                futureX += L / 4; // Move right
	            }
	        }

	        // Create and return the Rectangle object representing the future bounds
	        return new Rectangle(futureX, y, width, height);
	    }

	public String getMessage(){
		return message;
	}
}
