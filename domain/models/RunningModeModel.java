package domain.models;

import java.awt.event.KeyEvent;

import domain.objects.Fireball;
import domain.objects.Paddle;

public class RunningModeModel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Paddle paddle;
    private Fireball fireball;
    private long lastUpdateTime;

    public RunningModeModel() {
        // Initialize the paddle
        paddle = new Paddle(WIDTH / 2, HEIGHT - 50, WIDTH/10, 20); // Adjust parameters as needed

        // Initialize the fireball
        fireball = new Fireball(WIDTH / 2, HEIGHT / 2, 20, 20); // Adjust parameters as needed

        lastUpdateTime = System.currentTimeMillis();

    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Fireball getFireball() {
        return fireball;
    
    
    }
    long lastCollisionTime = 0; // Initialize the last collision time
    long cooldown = 1000; // Set the cooldown time in milliseconds (adjust as needed)

    public void update(long currentTime, boolean[] keys, int WIDTH, int HEIGHT) {
        // Calculate delta time (time elapsed since last update)
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // Convert to seconds
        lastUpdateTime = currentTime;

        // Continuous movement logic for the paddle
        if (keys[KeyEvent.VK_LEFT]) {
            paddle.setDeltaX(-1); // Move paddle left
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            paddle.setDeltaX(1); // Move paddle right
        }

        // Continuous rotation logic for the paddle
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

        
        // Check collision of fireball with paddle and apply cooldown
        if (fireball.collidesWithPaddle(paddle) && (currentTime - lastCollisionTime) >= cooldown) {
            fireball.reflectFromPaddle(paddle); // Reflect fireball when colliding with paddle
            lastCollisionTime = currentTime; // Update the last collision time
        }
    }
}
