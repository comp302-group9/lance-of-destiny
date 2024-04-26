package domain.objects.Barrier;

import java.awt.Color;
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
	public boolean isMoving = false;
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
	
	public boolean hasBarrierOnImmediateLeft(ArrayList<Barrier> barriers) {
        Rectangle bounds = getBounds();
        int leftBoundary = bounds.x - bounds.width; // Immediate left

        return barriers.stream().anyMatch(barrier ->
            barrier != this && barrier.getBounds().x <= leftBoundary &&
            Math.abs(barrier.getBounds().x - bounds.x) <= bounds.width
        );
    }

    // Check if there's a barrier immediately on the right
    public boolean hasBarrierOnImmediateRight(ArrayList<Barrier> barriers) {
        Rectangle bounds = getBounds();
        int rightBoundary = bounds.x + bounds.width; // Immediate right

        return barriers.stream().anyMatch(barrier ->
            barrier != this && barrier.getBounds().x >= rightBoundary &&
            Math.abs(barrier.getBounds().x - bounds.x) <= bounds.width
        );
    }
	
	public boolean checkIfMoving() {
        // Check if there is free space around the barrier in the x-axis
        // If there is free space, return true with a probability of 0.2
        return (new Random().nextDouble() < 0.2);
    }
	
	public void move(ArrayList<Barrier> barriers, double deltaTime) {
        
	}
	
	 public boolean isCollidingWithOtherBarriers(ArrayList<Barrier> barriers) {
	        Rectangle bounds = getBounds();
	        for (Barrier barrier : barriers) {
	            if (barrier != this && bounds.intersects(barrier.getBounds())) {
	                return true; // Collision detected
	            }
	        }
	        return false; // No collision
	    }

	 // Reverse the movement direction
	 public void reverseDirection() {
	     direction = (direction == 0) ? 1 : 0;
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
		g.setColor(Color.RED); // Set color to red
        g.drawLine(x, y, x + width, y); // Draw line for top hitbox

        // Draw the bottom hitbox (for debugging purposes)
        g.setColor(Color.BLUE); // Set color to blue
        g.drawLine(x, y + height, x + width, y + height); // Draw line for bottom hitbox
		
		g.setColor(Color.GREEN); // Set color to green
        g.drawLine(x, y, x, y + height); // Draw line for left hitbox

        // Draw the right hitbox (for debugging purposes)
        g.setColor(Color.YELLOW); // Set color to yellow
        g.drawLine(x + width, y, x + width, y + height); // Draw line for right hitbox
    
    }
	public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); 
    }
	public String getMessage(){
		return message;
	}
}