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

    
    public void checkCollisionWithBarriers(ArrayList<Barrier> barriers) {
        ArrayList<Barrier> barriersCopy = new ArrayList<>(barriers); // Create a copy of the list

        for (Barrier barrier : barriersCopy) { // Iterate over the copy
            Rectangle ballBounds = getBounds();
            Rectangle barrierBounds = barrier.getBounds();
        
            Point topLeft = new Point(ballBounds.x, ballBounds.y);
    Point topRight = new Point(ballBounds.x + ballBounds.width, ballBounds.y);
    Point bottomLeft = new Point(ballBounds.x, ballBounds.y + ballBounds.height);
    Point bottomRight = new Point(ballBounds.x + ballBounds.width, ballBounds.y + ballBounds.height);

    // Check if any of the corners intersect with the barrier
    if (barrierBounds.contains(topLeft) || barrierBounds.contains(topRight) || barrierBounds.contains(bottomLeft) || barrierBounds.contains(bottomRight)) {
        // Determine which side of the barrier it intersects with and reflect accordingly
        if (topLeft.y >= barrierBounds.y && bottomLeft.y <= barrierBounds.y + barrierBounds.height) {
            // Intersects with the left or right side of the barrier, reflect horizontally
            reflectHorizontal();
        } else {
            // Intersects with the top or bottom side of the barrier, reflect vertically
            reflectVertical();
        }
    }
        }
        
    }
    public void checkAndReflectCorners(Rectangle ballBounds, Rectangle barrierBounds) {
        
    }
    
}