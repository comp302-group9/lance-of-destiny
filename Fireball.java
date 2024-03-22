import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Fireball {

    private BufferedImage image;
    private int x, y;
    private int dx, dy;
    private int width, height;

    public Fireball(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Load the image from the class's resources
        try {
            image = ImageIO.read(getClass().getResource("images/Fireball.png")); // Adjust image path
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set initial velocity
        dx = 5; // Adjust as needed
        dy = -5; // Adjust as needed
    }

    public void move() {
        // Update the position of the fireball
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        // Draw the fireball
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } 
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); 
    }

    public void reflectHorizontal() {
        dy = -dy; // Reverse vertical direction
    }

    public void reflectVertical() {
        dx = -dx; // Reverse horizontal direction
    }

    // Check collision with paddle
    public boolean collidesWithPaddle(Paddle paddle) {
        return getBounds().intersects(paddle);
    }

    // Check collision with walls
    public void checkCollisionWithWalls(int screenWidth, int screenHeight) {
        if (x <= 0 || x + width >= screenWidth) {
            reflectVertical(); // Reflect if hitting left or right wall
        }
        if (y <= 0 || y + height >= screenHeight) {
            reflectHorizontal(); // Reflect if hitting top wall
        }
        // You may handle collision with bottom wall if necessary
    }
}
