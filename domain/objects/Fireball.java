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

    private void setDefaultVelocity(){
        dx = 5;
        dy = -5;
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
        
    }
    
    // Check collision with walls
    public void checkCollisionWithWalls(int screenWidth, int screenHeight) {
        if (x <= 0 || x + width >= screenWidth) {//right, left walls
            reflectVertical();
        }
        if (y <= 0 || y + height >= screenHeight) {//up, down walls (if want to drop ball, just add "y <= 0" in if statment)
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

    public void handleCollisionWithBarrier(Barrier barrier) {
    	
    	Rectangle ballBounds = getBounds();
        Rectangle barrierBounds = barrier.getBounds();

        int ballCenterX = ballBounds.x + ballBounds.width / 2;
        int ballCenterY = ballBounds.y + ballBounds.height / 2;

        int barrierCenterX = barrierBounds.x + barrierBounds.width / 2;
        int barrierCenterY = barrierBounds.y + barrierBounds.height / 2;

        // Calculate differences in centers
        int diffX = ballCenterX - barrierCenterX;
        int diffY = ballCenterY - barrierCenterY;

        boolean isVerticalCollision = Math.abs(ballCenterX - barrierCenterX) > Math.abs(ballCenterY - barrierCenterY);

        if (isVerticalCollision) {
            reflectVertical(); // If it's a vertical collision, reflect vertically
        } else {
            reflectHorizontal(); // If it's a horizontal collision, reflect horizontally
        }
        
        if (barrier.onHit()) { // If barrier should be destroyed
            barrier.destroy();
        }
    

        // Handle collision based on barrier type
        if (barrier instanceof SimpleBarrier) {
            // Bounce off the simple barrier
            reflectHorizontal();
            
        } else if (barrier instanceof ReinforcedBarrier) {
        	reflectHorizontal();
        	
        	// Handle collision with reinforced barrier
            // Implement appropriate behavior
        } else if (barrier instanceof ExplosiveBarrier) {
        	reflectHorizontal();
        	
        } else if (barrier instanceof RewardingBarrier) {
        	reflectHorizontal();
        	
        	// Handle collision with rewarding barrier
            // Implement appropriate behavior
        }
    }
    public void checkCollisionWithBarriers(ArrayList<Barrier> barriers) {
        ArrayList<Barrier> barriersCopy = new ArrayList<>(barriers); // Create a copy of the list

        for (Barrier barrier : barriersCopy) { // Iterate over the copy
            Rectangle ballBounds = getBounds();
            Rectangle barrierBounds = barrier.getBounds();

            if (ballBounds.intersects(barrierBounds)) { // If the fireball collides with the barrier

                // Determine if the impact is along the top/bottom or sides
                boolean hitTopBottom = (ballBounds.y + ballBounds.height <= (barrierBounds.y + barrierBounds.height / 2)+5) ||
                                       (ballBounds.y >= (barrierBounds.y + barrierBounds.height / 2)-5);
                
                boolean hitLR = (ballBounds.x + ballBounds.width <= (barrierBounds.x + barrierBounds.width / 2)+5) ||
                                       (ballBounds.x >= (barrierBounds.x + barrierBounds.width / 2)-5);
                if (hitTopBottom) {
                    reflectHorizontal(); // If hitting top/bottom, reflect horizontally
                }else if (hitLR){
                    reflectVertical(); // Otherwise, reflect vertically
                }

                if (barrier.onHit()) { // If the barrier should be destroyed
                    barriers.remove(barrier); // Safely remove it from the list
                }
            break;
            }
        }
    }
}