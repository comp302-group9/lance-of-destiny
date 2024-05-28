package domain.objects.Barrier;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import domain.models.RunningModeModel;

public abstract class Barrier {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected String name;
	protected String img;
	protected String freezeimg= "/ui/images/freeze.png";
	protected BufferedImage image;
	protected BufferedImage freezeImage;
	protected String message;
	public boolean isMoving = false;
    protected int direction;
	protected boolean frozen;
	protected Timer freezeTimer;
	protected int gridX, gridY;
	private int secondsElapsed;	

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
		this.width= RunningModeModel.barrierWidth;
		this.height =  RunningModeModel.barrierHeight;
		this.isMoving = checkIfMoving();
        if (isMoving) {
            this.direction = new Random().nextBoolean() ? -1 : 1; // Random initial direction   
        }
		try {
			this.image = ImageIO.read(getClass().getResource(this.getImg()));
			this.freezeImage = ImageIO.read(getClass().getResource(freezeimg));
        } catch (IOException e) {
            e.printStackTrace();
        }
		freezeTimer = new Timer(1000, e -> {
            if (secondsElapsed < 15) {
                secondsElapsed++;
                // Do something here if needed, like updating UI
            } else {
                freezeTimer.stop();
				unfreeze();
            }
        });
		
		freeze();
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
	     direction = -direction;
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

	public int getGridX() {
		return gridX;
	}

	public void setGridX(int gridX) {
		this.gridX = gridX;
	}

	public int getGridY() {
		return gridY;
	}

	public void setGridY(int gridY) {
		this.gridY = gridY;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = name;
	}

	public Boolean getFrozen() {
		return frozen;
	}

	public void draw(Graphics g) {
        // Draw the fireball
        if (image != null) {
            g.drawImage(image, x, y, RunningModeModel.barrierWidth,  RunningModeModel.barrierHeight, null);
        } 
		Graphics2D g2d = (Graphics2D) g.create();

    
    if(frozen){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
		g2d.drawImage(freezeImage, x, y, RunningModeModel.barrierWidth, RunningModeModel.barrierHeight, null);
        // Calculate position and size of the timer circle
        int circleSize = 12; // Size of the circle
        int padding = 4; // Padding from top and right edges
        int circleX = x + width - circleSize + padding; // X position of the circle
        int circleY = y - padding; // Y position of the circle

        // Calculate the angle to draw the filled arc
        double fillPercentage = (double) secondsElapsed / 15;
        int fillAngle = (int) Math.round(fillPercentage * 360);

		g2d.setColor(Color.BLACK);
        g2d.drawArc(circleX, circleY, circleSize, circleSize, 90, 360);
        // Draw the filled arc
        g2d.setColor(Color.BLUE);
        g2d.fillArc(circleX, circleY, circleSize, circleSize, 90, -fillAngle);
    }

    g2d.dispose();
    }

	public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); 
    }

	public String getMessage(){
		return message;
	
    }

	public void freeze() {
        frozen = true;
        freezeTimer.start();
    }

	public void unfreeze() {
        frozen = false;
    }
}