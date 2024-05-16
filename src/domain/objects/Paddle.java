package domain.objects;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import PhysicsEngines.PhysicsObject;
import PhysicsEngines.Vector;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Paddle implements PhysicsObject{

    private BufferedImage image;
    private int x;
    private int y;
    private Vector position;
    private Vector velocity;
    
    private int width;
    private int height;
    private int paddleSpeed = 6;
    private double rotationAngle = 0;
    private double rotationSpeed = 20; // degrees per second
    private int direction = 0;

    public Paddle (int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        position = new Vector(x, y);
        velocity = new Vector(0,0);
        this.width = width;
        this.height = height;
        
        // Load the image from the class's resources
        try {
            image = ImageIO.read(getClass().getResource("/ui/images/Player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public double getRotationAngle() {return rotationAngle;}
    public int getDirection() {return direction;}
    public void setDirection(int direction) {this.direction= direction;}
    public Vector getPosition() {
        return position;
    }
    public boolean isMoving() {
        return velocity.getMagnitude()!=0;// Check if paddle is moving
    }
    public Vector getVelocity() {
        return velocity;
    }

    // Responsible for left-right movement
    public void setDeltaX(int xDirection, int gameWidth) {
        int dx = xDirection * paddleSpeed;
        velocity.setX(dx);
        x += dx;
        position.setX(x);
        
     // Constrain the paddle within the game boundaries
        if (x < 0) {
            x = 0;
            position.setX(x);
        } else if (x + width > gameWidth) {
            x = gameWidth - width;
            position.setX(x);
        }
    }

    // Responsible for right rotation
    public void rotateClockwise(double deltaTime) {
        rotationAngle += rotationSpeed * deltaTime;
        if (rotationAngle > 45) {
            rotationAngle = 45;
        }
    }

    // Responsible for left rotation
    public void rotateAntiClockwise(double deltaTime) {
        rotationAngle -= rotationSpeed * deltaTime;
        if (rotationAngle < -45) {
            rotationAngle = -45;
        }
    }

    public void resetRotation(double deltaTime) {
        if (rotationAngle > 0) {
            rotationAngle -= rotationSpeed * deltaTime;
            if (rotationAngle < 0) rotationAngle = 0; // Prevent overshooting
        } else if (rotationAngle < 0) {
            rotationAngle += rotationSpeed * deltaTime;
            if (rotationAngle > 0) rotationAngle = 0;
        }
    }

    // Draws the paddle
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(rotationAngle), x + width / 2, y + height / 2);
        g2d.drawImage(image, x, y, width, height, null);
        g2d.dispose(); 
    }

    // To get the boundries of the rotated paddle
    public Shape getBounds() {
        AffineTransform transform = new AffineTransform();
        transform.translate(x + width / 2, y + height / 2);// Translating transform object to the center of paddle
        transform.rotate(Math.toRadians(rotationAngle), 0, 0); // Rotate around the center
        Rectangle2D paddleRectangle = new Rectangle2D.Double(-width / 2, -height / 2, width, height); // Centered rectangle
        Shape transformedRect = transform.createTransformedShape(paddleRectangle);// Apply transformation to new rectangle
        return transformedRect;
    }
}