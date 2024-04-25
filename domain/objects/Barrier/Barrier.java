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
	public boolean isMoving;
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
	
	public void move(ArrayList<Barrier> barriers, double deltaTime) {
        if (isMoving) {
            
        	double movementDistance = (33 * 900 / 512) / 4; // L/4
            double movement = movementDistance * deltaTime; // Scale by delta time
        	
        	if (direction == 0) {
                x -= 10; // Move left
            } else {
                x += 10; // Move right
            }

            // Check for collisions with other barriers
            if (isCollidingWithOtherBarriers(barriers)) {
                reverseDirection(); // Reverse direction if collision is detected
            }
            
            if (x < 0 || x + width > RunningModeModel.WIDTH) {
                reverseDirection(); // Reverse if hitting screen boundaries
            }
        }
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
    }
	public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); 
    }
	 public Rectangle getFutureBounds() {
	        // Calculate the future x-coordinate based on the current direction
	        int futureX = x;
	        if (isMoving) {
	            if (direction == 0) {
	                //futureX -= L / 4; // Move left
	            } else {
	                //futureX += L / 4; // Move right
	            }
	        }

	        // Create and return the Rectangle object representing the future bounds
	        return new Rectangle(futureX, y, width, height);
	    }
	 public void destroy() {
	        System.out.println("Destroying Simple Barrier...");
	        
	        ArrayList<Barrier> barrierList = RunningModeModel.barriers;
	        
	        if (barrierList != null) {
	            barrierList.remove(this); // Remove this barrier from the list
	        }

	        // Optional cleanup logic if needed
	        this.image = null; // Clear image reference
	    } 

	public String getMessage(){
		return message;
	}
}