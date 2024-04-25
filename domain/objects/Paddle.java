package domain.objects;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Paddle {

    private BufferedImage image;
    private int x;
    private int y;
    private int width;
    private int height;
    private int paddleSpeed = 6;
    private double rotationAngle = 0;
    private double rotationSpeed = 20; // degrees per second

    public Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
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

    // Responsible for left-right movement
    public void setDeltaX(int xDirection) {
        int dx = xDirection * paddleSpeed;
        x += dx;
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

    // Draws the paddle
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(rotationAngle), x + width / 2, y + height / 2);
        g2d.drawImage(image, x, y, width, height, null);
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