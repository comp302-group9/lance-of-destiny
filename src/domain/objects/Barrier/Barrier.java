package domain.objects.Barrier;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import domain.models.RunningModeModel;
import PhysicsEngines.*;
public abstract class Barrier implements PhysicsObject {

	protected int x;
	protected int y;
	protected int width;
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	protected int height;
	protected String name;
	protected String img;
	protected BufferedImage image;
	protected String message;
	private Vector position;
	private Vector velocity;
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
		this.position= new Vector(x,y);
		this.velocity= new Vector(0,0);
		this.width= RunningModeModel.barrierWidth;
		this.height =  RunningModeModel.barrierHeight;
		this.isMoving = checkIfMoving();
        if (isMoving) {
            this.direction = new Random().nextBoolean() ? -1 : 1; // Random initial direction
            this.velocity.setX(direction);
        }
		try {
			this.image = ImageIO.read(getClass().getResource(this.getImg()));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public Vector getVelocity() {
		return velocity;
	}
	public boolean isMoving() {
		return isMoving;
	}
	public Vector getPosition() {
		return position;
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
		 return !hasBarrierOnImmediateLeft(RunningModeModel.barriers) && 
	               !hasBarrierOnImmediateRight(RunningModeModel.barriers) && 
	               (new Random().nextDouble() < 0.2);
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
	     direction = (direction == 1) ? -1 : 1;
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
	public Vector getCollisionNormal(PhysicsObject fireball) {
        // Calculate collision normal vector based on fireball's position relative to barrier
        double dx = fireball.getPosition().getX() - getX();
        double dy = fireball.getPosition().getY() - getY();
        Vector normal = new Vector(dx, dy);
        normal.normalize();
        return normal; // Return normalized vector
    }
	public double getReflectionAngle(Vector collisionNormal) {
	    // Calculate the angle between the current velocity vector and the collision normal
	    double theta = Math.atan2(collisionNormal.getY(), collisionNormal.getX());
	    
	    // Calculate the angle of reflection relative to the collision normal
	    double reflectionAngle = 2 * theta - Math.PI;
	    
	    // Ensure the reflection angle is within the range [0, 2*PI]
	    if (reflectionAngle < 0) {
	        reflectionAngle += 2 * Math.PI;
	    }
	    
	    return reflectionAngle;
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

	public void setMoving() {
		this.isMoving = true;
		this.direction= 1;// TODO Auto-generated method stub
		
	}
}