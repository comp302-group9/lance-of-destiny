import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

public class GamePanel extends JPanel implements KeyListener, Runnable {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Paddle paddle;
    private Fireball fireball; // Add Fireball object
    private BufferedImage backgroundImage;
    private boolean[] keys;
    private long lastUpdateTime;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this); // Add key listener to the panel
        setFocusable(true); // Make panel focusable to receive key events

        // Load the background image
        try {
            URL imageURL = getClass().getResource("images/Background.png");
            backgroundImage = ImageIO.read(imageURL);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize the paddle
        paddle = new Paddle(WIDTH / 2, HEIGHT - 50, 100, 20); // Adjust parameters as needed

        // Initialize the fireball
        fireball = new Fireball(WIDTH / 2, HEIGHT / 2, 20, 20); // Adjust parameters as needed

        // Initialize the keys array
        keys = new boolean[256];

        // Initialize last update time
        lastUpdateTime = System.currentTimeMillis();

        // Start the thread for continuous movement and rotation
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            // Calculate delta time (time elapsed since last update)
            long currentTime = System.currentTimeMillis();
            double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // Convert to seconds
            lastUpdateTime = currentTime;

            // Continuous movement logic
            if (keys[KeyEvent.VK_LEFT]) {
                paddle.setDeltaX(-1); // Move paddle left
            }
            if (keys[KeyEvent.VK_RIGHT]) {
                paddle.setDeltaX(1); // Move paddle right
            }

            // Continuous rotation logic
            if (keys[KeyEvent.VK_A]) {
                paddle.rotateAntiClockwise(deltaTime); // Rotate paddle anti-clockwise
            }
            if (keys[KeyEvent.VK_D]) {
                paddle.rotateClockwise(deltaTime); // Rotate paddle clockwise
            }

            // Move the fireball
            fireball.move();

            // Check collision of fireball with walls
            fireball.checkCollisionWithWalls(WIDTH, HEIGHT);

            // Check collision of fireball with paddle
            if (fireball.collidesWithPaddle(paddle)) {
                fireball.reflectHorizontal(); // Reflect fireball when colliding with paddle
            }

            // Repaint the panel
            repaint();

            // Pause for a short duration to prevent high CPU usage
            try {
                Thread.sleep(10); // Adjust the sleep duration as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the paddle
        paddle.draw(g);

        // Draw the fireball
        fireball.draw(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keys[keyCode] = true; // Mark the key as pressed
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keys[keyCode] = false; // Mark the key as released
    }
}
