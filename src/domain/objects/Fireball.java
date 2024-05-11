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

public class Fireball {

    private BufferedImage image;
    private int x, y;
    private double dx, dy;
    private int width, height;
    private boolean isLaunched = false; // Track if the fireball is launched

    public Fireball(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
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
    

	public int getHeight() {
		return height;
	
	}
	
	public int getWidth() {
		return width;
	}
	

	public int getY() {
		return y;
	}
	
	public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
	
	public void resetPosition(int paddleX, int paddleY) {
        setPosition(paddleX, paddleY); // Reset to a given position
    }

    private void setDefaultVelocity(){
		dx = 3;
        dy = 3;
    }

    //updates the fireball location
    public void move() {
        x += dx;
        y += dy;
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
    
    public void reflectFromPaddle(Paddle paddle) {
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
    }

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
        
            Point topLeft = new Point(ballBounds.x, ballBounds.y);
        Point topRight = new Point(ballBounds.x + ballBounds.width, ballBounds.y);
        Point bottomLeft = new Point(ballBounds.x, ballBounds.y + ballBounds.height);
        Point bottomRight = new Point(ballBounds.x + ballBounds.width, ballBounds.y + ballBounds.height);
            double px = 0;
            double py = 0;

            double middleTopX = ballBounds.getCenterX();
            double middleTopY = ballBounds.getMinY();
            
            double middleLeftX = ballBounds.getMinX();
            double middleLeftY = ballBounds.getCenterY();
            
            double middleRightX = ballBounds.getMaxX();
            double middleRightY = ballBounds.getCenterY();
            
            double middleBottomX = ballBounds.getCenterX();
            double middleBottomY = ballBounds.getMaxY();
            
            double offsetx = -2;
            double offsety =  -2;

            boolean isMiddleTopInside = (middleTopX >= barrierBounds.getMinX() + offsetx && middleTopX <= barrierBounds.getMaxX() - offsetx)
        && (middleTopY >= barrierBounds.getMinY() + offsety && middleTopY <= barrierBounds.getMaxY() - offsety);

            boolean isMiddleLeftInside = (middleLeftX >= barrierBounds.getMinX() + offsetx && middleLeftX <= barrierBounds.getMaxX() - offsetx)
        && (middleLeftY >= barrierBounds.getMinY() + offsety && middleLeftY <= barrierBounds.getMaxY() - offsety);

            boolean isMiddleRightInside = (middleRightX >= barrierBounds.getMinX() + offsetx && middleRightX <= barrierBounds.getMaxX() - offsetx)
        && (middleRightY >= barrierBounds.getMinY() + offsety && middleRightY <= barrierBounds.getMaxY() - offsety);

            boolean isMiddleBottomInside = (middleBottomX >= barrierBounds.getMinX() + offsetx && middleBottomX <= barrierBounds.getMaxX() - offsetx)
        && (middleBottomY >= barrierBounds.getMinY() + offsety && middleBottomY <= barrierBounds.getMaxY() - offsety);


            boolean isTopLeftInside = (topLeft.getX() >= barrierBounds.getMinX() && topLeft.getX() <= barrierBounds.getMaxX())
        && (topLeft.getY() >= barrierBounds.getMinY() && topLeft.getY() <= barrierBounds.getMaxY());

            boolean isTopRightInside = (topRight.getX() >= barrierBounds.getMinX() && topRight.getX() <= barrierBounds.getMaxX())
        && (topRight.getY() >= barrierBounds.getMinY() && topRight.getY() <= barrierBounds.getMaxY());

            boolean isBottomLeftInside = (bottomLeft.getX() >= barrierBounds.getMinX() && bottomLeft.getX() <= barrierBounds.getMaxX())
        && (bottomLeft.getY() >= barrierBounds.getMinY() && bottomLeft.getY() <= barrierBounds.getMaxY());

            boolean isBottomRightInside = (bottomRight.getX() >= barrierBounds.getMinX() && bottomRight.getX() <= barrierBounds.getMaxX())
        && (bottomRight.getY() >= barrierBounds.getMinY() && bottomRight.getY() <= barrierBounds.getMaxY());

        if(isMiddleBottomInside||isMiddleTopInside){
            reflectHorizontal();
            if (!barrier.getFrozen() && barrier.onHit()) { // If the barrier should be destroyed
                barriers.remove(barrier); // Safely remove it from the list
            }break;
        }
        else if(isMiddleLeftInside||isMiddleRightInside){
            if (!barrier.getFrozen() && barrier.onHit()) { // If the barrier should be destroyed
                barriers.remove(barrier); // Safely remove it from the list
            }
            reflectVertical();            
            break;
        }
        else if (isTopLeftInside || isTopRightInside){
            if(ballBounds.getMaxY()<barrierBounds.getMaxY()+ballBounds.getHeight()-py){
                reflectVertical();if (!barrier.getFrozen() &&barrier.onHit()) { // If the barrier should be destroyed
                    barriers.remove(barrier); // Safely remove it from the list
                }break;
            }else if((ballBounds.getMaxX()<barrierBounds.getMaxX()+ballBounds.getWidth()-px)||ballBounds.getX()>barrierBounds.getX()-ballBounds.getWidth()+px){
                reflectHorizontal();if (!barrier.getFrozen() &&barrier.onHit()) { // If the barrier should be destroyed
                    barriers.remove(barrier); // Safely remove it from the list
                }break;
            }
            
        }else if(isBottomLeftInside || isBottomRightInside){
            if(ballBounds.getY()>barrierBounds.getY()-ballBounds.getHeight()+py){
                reflectVertical();if (!barrier.getFrozen() &&barrier.onHit()) { // If the barrier should be destroyed
                    barriers.remove(barrier); // Safely remove it from the list
                }break;
            }else if((ballBounds.getMaxX()<barrierBounds.getMaxX()+ballBounds.getWidth()-px)||ballBounds.getX()>barrierBounds.getX()-ballBounds.getWidth()+px){
                reflectHorizontal();if (!barrier.getFrozen() &&barrier.onHit()) { // If the barrier should be destroyed
                    barriers.remove(barrier); // Safely remove it from the list
                }break;
            }
        }
        }
    }
}