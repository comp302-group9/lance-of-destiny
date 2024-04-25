package domain.models;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;
import domain.objects.Barrier.ExplosiveBarrier;
import domain.objects.Barrier.ReinforcedBarrier;
import domain.objects.Barrier.RewardingBarrier;
import domain.objects.Barrier.SimpleBarrier;
import ui.screens.BuildingModeView;

public class RunningModeModel {
    public static final int WIDTH = BuildingModeView.WIDTH;
    public static final int HEIGHT = BuildingModeView.HEIGHT;
    public static int barrierWidth = 33 * WIDTH / 512;
	public static int barrierHeight = HEIGHT / 30;
    private static final int ROWS = BuildingModeModel.ROWS;
    private static final int COLUMNS = BuildingModeModel.COLUMNS;
    private Paddle paddle;
    private Fireball fireball;
    private long lastUpdateTime;
    private boolean paused = false; 
    public static ArrayList<Barrier> barriers = new ArrayList<Barrier>();

    public RunningModeModel() {
        // Initialize the paddle
        paddle = new Paddle(WIDTH / 2, HEIGHT - 50, WIDTH/10, 20); // Adjust parameters as needed

        // Initialize the fireball
        fireball = new Fireball( WIDTH / 2, 7 * HEIGHT / 8, 20, 20); // Adjust parameters as needed

        lastUpdateTime = System.currentTimeMillis();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    public boolean isPaused() {
        return paused;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Fireball getFireball() {
        return fireball;
    }
    long lastCollisionTime = 0; // Initialize the last collision time
    long lastCollisionTime2 = 0;
    long cooldown = 1000; // Set the cooldown time in milliseconds (adjust as needed)
    long cooldownbar = 15;

    public void update(long currentTime, boolean[] keys) {
        // Calculate delta time (time elapsed since last update)
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // Convert to seconds
        lastUpdateTime = currentTime;
        
        
        for (Barrier barrier : barriers) {
            if (barrier.isMoving) {
                barrier.move(barriers, deltaTime); // Move barrier and check for collisions
            }
        }

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
        fireball.checkCollisionWithBarriers(barriers);
        if ((currentTime - lastCollisionTime2) >= cooldownbar) {
        	fireball.checkCollisionWithBarriers(barriers);
        	lastCollisionTime2 = currentTime;
        }
        
        // Check collision of fireball with paddle and apply cooldown
        if (fireball.collidesWithPaddle(paddle) && (currentTime - lastCollisionTime) >= cooldown) {
            fireball.reflectFromPaddle(paddle); // Reflect fireball when colliding with paddle
            lastCollisionTime = currentTime; // Update the last collision time
        }
    }

    public void initaliseBarrierLocations(int[][] grid){
        int xStart = HEIGHT / 32;
		int yStart = WIDTH / 32;
		int xGap = HEIGHT / 128;
		int yGap = WIDTH / 96;
        for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
                int x = xStart + col * (barrierWidth + xGap);
				int y = yStart + row * (barrierHeight + yGap);
				switch (grid[row][col]) {
					case 0:
                        break;
                    case 1:
                        Barrier simple = new SimpleBarrier(x,y);
                        barriers.add(simple);
                        break;
                    case 2:
                        Barrier reinforced = new ReinforcedBarrier(x,y);
                        barriers.add(reinforced);
                        break;
                    case 3:
                        Barrier explosive = new ExplosiveBarrier(x,y);
                        barriers.add(explosive);
                        break;
                    case 4:
                        Barrier rewarding = new RewardingBarrier(x,y);
                        barriers.add(rewarding);
                        break;
                }
			}
		}
    }
}