package domain.objects;
import java.awt.*;
import java.awt.geom.Area;

import javax.swing.*;

import domain.objects.Paddle;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import domain.objects.Barrier.*;
import PhysicsEngines.*
;public class Fireball implements PhysicsObject {

    private BufferedImage image;
    private int x, y;
    private double dx, dy;
    private Vector position;
    private Vector velocity;
    private int width, height;
    private boolean isLaunched = false; // Track if the fireball is launched

    public Fireball(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        position = new Vector(x, y);
        velocity = new Vector(1, -1);
        velocity.setMagnitude(5);
        this.width = width;
        this.height = height;
        

        // Load the image from the class's resources
        try {
            image = ImageIO.read(getClass().getResource("/ui/images/Fireball.png")); // Adjust image path
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultVelocity();
    }
    
public int getRadius() {
	return width;
}
	public int getHeight() {
		return height;
	
	}
	
	public int getWidth() {
		return width;
	}
	

	public int getY() {
		return y;
	}
	public Vector getPosition() {
        return position;
    }
	 public boolean isMoving() {
	        return isLaunched; // Fireball is always moving
	    }
	 public Vector getVelocity() {
	        return velocity;
	    }
	
	public void setPosition(int x, int y) {
        
        this.position.setX(x);
        this.position.setY(y);
        this.x = x;
        this.y = y;
    }
	
	public void resetPosition(int paddleX, int paddleY) {
        setPosition(paddleX, paddleY); // Reset to a given position
    }

    private void setDefaultVelocity(){
		dx = 1;
        dy = -1;
        velocity.setX(dx);
        velocity.setY(dy);
    }

    //updates the fireball location
    public void move() {
        x += dx;
        y += dy;
        position.setX(x);
        position.setY(y);
    }
   
    public void reflectFromObject(Vector normal) {
        velocity.reflect(normal);
    }
    public void handleMovingObjectCollision(Vector objectVelocity, Vector collisionNormal) {
        // Calculate the component of fireball velocity parallel to object movement
        Vector parallelVelocity = new Vector(velocity.getX(), velocity.getY());
        parallelVelocity.normalize();
        double parallelComponent = parallelVelocity.dotProduct(objectVelocity);

        // Check if object movement direction is same, opposite, or perpendicular to fireball velocity
        if (parallelComponent > 0) {
            // Same direction: Reflect according to non-moving object rules
            reflectFromObject(collisionNormal);
            // Increase speed
            velocity.setMagnitude(velocity.getMagnitude() + 5);
        } else if (parallelComponent < 0) {
            // Opposite direction: Reflect 180 degrees
            velocity.reflect(collisionNormal);
        } else {
            // Perpendicular: Reflect 45 degrees
            Vector reflectionDirection = new Vector(collisionNormal.getY(), -collisionNormal.getX());
            velocity.reflect(reflectionDirection);
        }
    }

    public void reflectHorizontal() {dy = -dy;}
    public void reflectVertical() {dx = -dx;}
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); 
    }
    
    public void launch(int paddleX, int paddleY) {
        if (!isLaunched) { // Launch only if not already launched
        	// Center the fireball horizontally on the paddle
            int fireballX = paddleX + (width / 2); // Make sure 'width' is accessible
            // Place the fireball just above the paddle
            int fireballY = paddleY - height; // Ensure 'height' is defined

            // Set the fireball's position
            setPosition(fireballX, fireballY); // Call to set the new position

            isLaunched = true; // Update launch status
        }
    }

    public boolean isLaunched() {
        return isLaunched; // Check if the fireball is launched
    }
    
    // Check collision with paddle
    public boolean collidesWithPaddle(Paddle paddle) {
        Area ballArea = new Area(getBounds());
        Area paddleArea = new Area(paddle.getBounds());
        ballArea.intersect(paddleArea);
        return !ballArea.isEmpty();
    }
    
    /*public void reflectFromPaddle(Paddle paddle) {
        // Calculate initial velocity magnitude of the ball
        double v_ball_initial = Math.sqrt(dx * dx + dy * dy); // initial velocity of the ball in m/s
        
        // angle of the wall in radians
        double theta_wall = Math.toRadians(paddle.getRotationAngle()); // angle of the wall in radians
        
        // Calculate initial velocity angle of the ball
        double theta_ball = Math.atan2(dy, dx); // angle of initial velocity of the ball
        
        // Calculate angle between initial velocity of the ball and the wall
        double alpha = theta_wall - theta_ball;
        
        // Calculate final velocity components of the ball after collision using reflection law
        dx = v_ball_initial * Math.cos(theta_wall + alpha);
        dy = v_ball_initial * Math.sin(theta_wall + alpha);
        
        if((dx<0&&paddle.getDirection()>0)||(dx>0&&paddle.getDirection()<0)){
           dx*=-1;
        }
    }*/

    public void validateSpeed(Paddle paddle){
        float increase = 5/64;
        if((dx>0&&paddle.getDirection()>0)||(dx<0&&paddle.getDirection()<0)){
            double currentSpeed = Math.sqrt(dx * dx + dy * dy);

        // Calculate the ratio of the new speed to the current speed
        double ratio = (currentSpeed + increase) / currentSpeed;

        // Increase the speed while maintaining direction
        dx *= ratio;
        dy *= ratio;
        }
    }
    /*public void reflect(Vector collisionNormal, Vector objectMovementDirection) {
        // Calculate the dot product of the fireball velocity and the collision normal
        double dotProduct = velocity.dotProduct(collisionNormal);
        System.out.println("I am here (reflect method)\n");
        // Check if the object is moving in the same direction as the component of the fireball velocity parallel to its movement direction
        double parallelComponent = velocity.projectOnto(objectMovementDirection).getMagnitude();
        boolean movingInSameDirection = objectMovementDirection.dotProduct(velocity) > 0;

        if (movingInSameDirection) {
            // Apply the same reflection rules as for non-moving objects
            Vector reflection = velocity.subtract(collisionNormal.multiply(2 * dotProduct));
            setVelocity(reflection);

            // Increase speed by 5px/second
            double speedIncrease = 5.0; // Adjust as needed
            setVelocity(velocity.add(objectMovementDirection.normalize().multiply(speedIncrease)));
        } else {
            if (dotProduct > 0) {
                // Apply reflection with angle equal to and symmetric around the normal of the hit surface
                Vector reflection = velocity.subtract(collisionNormal.multiply(2 * dotProduct));
                setVelocity(reflection);
            } else {
                // Apply reflection with angle of 180 degrees relative to the line of the original fireball movement direction
                Vector originalDirection = velocity.normalize();
                Vector reflection = originalDirection.subtract(collisionNormal.multiply(2 * originalDirection.dotProduct(collisionNormal)));
                setVelocity(reflection);
            }
        }
    }*/
    public void reflect(Vector collisionNormal, Vector objectMovementDirection) {
        // Calculate the dot product of the fireball velocity and the collision normal
        double dotProduct = velocity.dotProduct(collisionNormal);

        // Check if the object is moving in the same direction as the component of the fireball velocity parallel to its movement direction
        boolean movingInSameDirection = objectMovementDirection.dotProduct(velocity) > 0;

        if (movingInSameDirection) {
            // Apply the same reflection rules as for non-moving objects
            Vector reflection = velocity.subtract(collisionNormal.multiply(2 * dotProduct));

            // Increase speed by 5px/second
            double speedIncrease = 5.0; // Adjust as needed
            Vector parallelVelocity = velocity.projectOnto(objectMovementDirection);
            Vector speedIncreaseVector = objectMovementDirection.normalize().multiply(speedIncrease);
            Vector newVelocity = reflection.add(speedIncreaseVector);
            setVelocity(newVelocity);
        } else {
            if (dotProduct > 0) {
                // Apply reflection with angle equal to and symmetric around the normal of the hit surface
                Vector reflection = velocity.subtract(collisionNormal.multiply(2 * dotProduct));
                setVelocity(reflection);
            } else {
                // Apply reflection with angle of 180 degrees relative to the line of the original fireball movement direction
                Vector originalDirection = velocity.normalize();
                Vector reflection = originalDirection.subtract(collisionNormal.multiply(2 * originalDirection.dotProduct(collisionNormal)));
                setVelocity(reflection);
            }
        }
    }
private void setVelocity(Vector newVelocity) {
		this.velocity= newVelocity;
		this.dx = velocity.getX();
		this.dy= velocity.getY();
		
	}
public void setVelocity(int dx, int dy) {
	velocity.setX(dx);
	velocity.setY(dy);
}
    
    // Check collision with walls
    public void checkCollisionWithWalls(int screenWidth, int screenHeight) {
        if (x <= 0 || x + width >= screenWidth) {//right, left walls
            reflectVertical();
        }
        if (y <= 0) {//up, down walls (if want to drop ball, just add "y <= 0" in if statment)
            reflectHorizontal();
        }
    }

    public void draw(Graphics g) {
        // Draw the fireball
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } 
    }
    // Check collision with barriers
    public boolean collidesWithBarrier(Barrier barrier) {
        Rectangle ballBounds = getBounds();
        Rectangle barrierBounds = barrier.getBounds();
        return ballBounds.intersects(barrierBounds);
    }

    public void checkCollisionWithBarriers(ArrayList<Barrier> barriers) {
        ArrayList<Barrier> barriersCopy = new ArrayList<>(barriers); // Create a copy of the list

        for (Barrier barrier : barriersCopy) { // Iterate over the copy
            Rectangle ballBounds = getBounds();
            Rectangle barrierBounds = barrier.getBounds();

            if (ballBounds.intersects(barrierBounds)) { // If the fireball collides with the barrier
            	if (barrier.isMoving) {
            		handleMovingObjectCollision(barrier.getVelocity(),barrier.getCollisionNormal(this));
            	}
            	else {
            		
            	}
            }
        }
    }





}