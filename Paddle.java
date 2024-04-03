import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
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
            image = ImageIO.read(getClass().getResource("images/Player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDeltaY(int yDirection) {
        int dy = yDirection * paddleSpeed;
        y += dy;
    }

    public void setDeltaX(int xDirection) {
        int dx = xDirection * paddleSpeed;
        x += dx;
    }

    public void rotateClockwise(double deltaTime) {
        rotationAngle += rotationSpeed * deltaTime;
        if (rotationAngle > 45) { // Ensure rotation is within bounds
            rotationAngle = 45;
        }
    }

    public void rotateAntiClockwise(double deltaTime) {
        rotationAngle -= rotationSpeed * deltaTime;
        if (rotationAngle < -45) { // Ensure rotation is within bounds
            rotationAngle = -45;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(rotationAngle), x + width / 2, y + height / 2); // Rotate around center of paddle
        
        // Draw the image onto the paddle
        if (image != null) {
            g2d.drawImage(image, x, y, width, height, null);
        } else {
            // If image fails to load, draw a colored rectangle as fallback
            g2d.setColor(new Color(160, 160, 255));
            g2d.fillRect(x, y, width, height);
        }
        
        g2d.dispose();
    }

    public Shape getBounds() {
        AffineTransform transform = new AffineTransform();
        // Translate to the center of the rectangle (assuming x and y represent top-left corner)
        transform.translate(x + width / 2, y + height / 2);
        transform.rotate(Math.toRadians(rotationAngle), 0, 0); // Rotate around the center
        Rectangle2D paddleRectangle = new Rectangle2D.Double(-width / 2, -height / 2, width, height); // Centered rectangle
        Shape transformedRect = transform.createTransformedShape(paddleRectangle);
        
        // Get the bounding box of the transformed shape
        Rectangle bounds = transformedRect.getBounds();
        
        // Translate the bounding box back to the original position
        bounds.translate((int)(x + width / 2 - bounds.getCenterX()), (int)(y + height / 2 - bounds.getCenterY()));
    
        // Return a new Rectangle based on the bounding box
        return transformedRect;
    }
    
    
}
