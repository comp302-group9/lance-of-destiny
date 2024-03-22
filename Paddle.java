import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Paddle extends Rectangle {

    private BufferedImage image;
    private int paddleSpeed = 6;
    private double rotationAngle = 0;
    private double rotationSpeed = 20; // degrees per second

    Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
         
        // Load the image from the class's resources
        try {
            image = ImageIO.read(getClass().getResource("images/Player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
