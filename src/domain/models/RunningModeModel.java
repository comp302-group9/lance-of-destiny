package domain.models;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import domain.objects.Box;
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
    public static int barrierWidth = 51;
    public static int barrierHeight =15;
    private static final int ROWS = BuildingModeModel.ROWS;
    private static final int COLUMNS = BuildingModeModel.COLUMNS;
    private Paddle paddle;
    private Fireball fireball;
    private long lastUpdateTime;
    private boolean paused = false; 
    public static ArrayList<Barrier> barriers = new ArrayList<Barrier>();
    public static ArrayList<Box> boxes= new ArrayList<Box>();
    private Random random=new Random();
    private boolean gameOver = false; // State to track if the game is over
    private String gameOverMessage = "Game Over!"; // Game over message
    private int chances = 3; // Add this line to keep track of player's chances

    private long lastCollisionTime = 0; // Initialize the last collision time
    private long lastCollisionTime2 = 0;
    private long cooldown = 1000; // Set the cooldown time in milliseconds (adjust as needed)
    private long cooldownbar = 15;

    public RunningModeModel() {
        // Initialize the paddle
        paddle = new Paddle(WIDTH / 2, HEIGHT - 50, WIDTH/10, 20); // Adjust parameters as needed

        // Initialize the fireball
        fireball = new Fireball( WIDTH / 2, 7 * HEIGHT / 8, 16, 16); // Adjust parameters as needed

        lastUpdateTime = System.currentTimeMillis();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public boolean isGameOver() {
        return gameOver; // Return the game-over state
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver; // Set the game-over state
    }
    
    public String gameOverMessage() { // New method to return the game-over message
        return gameOverMessage; // Return the game-over message
    }
    
    public void restart() {
        // Logic to restart the game (reset positions, scores, etc.)
        setGameOver(false); // Reset game-over state
        fireball.setPosition(paddle.getX() + paddle.getWidth() / 2 - fireball.getWidth() / 2, paddle.getY() - fireball.getHeight());
        fireball.setLaunched(false); // Ensure fireball is not launched
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Fireball getFireball() {
        return fireball;
    }

    public int getChances() {
        return chances;
    }

    public void decreaseChance() {
        chances--;
        if (chances <= 0) {
            setGameOver(true);
        } else {
            restart(); // Restart the fireball position when a chance is lost
        }
    }

    public void update(long currentTime, boolean[] keys) {
        // Calculate delta time (time elapsed since last update)
    	// If the game is over, return early to stop game logic

    	double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // Convert to seconds
        lastUpdateTime = currentTime;
        
        for (int i=0; i<boxes.size() ; i++){
            Box box = boxes.get(i);
            box.move();
            if (box.getY() > HEIGHT) {
                boxes.remove(i);
                i--;
            }
        }
        
        for (Barrier barrier : barriers) {
            if (barrier.isMoving) {
                barrier.move(barriers, deltaTime); // Move barrier and check for collisions
            }
        }
        
        if (!fireball.isLaunched()) {
            // Align fireball's x-coordinate to the paddle's center
            int fireballX = paddle.getX() + (paddle.getWidth() - fireball.getWidth()) / 2;
            // Align fireball's y-coordinate to the top edge of the paddle
            int fireballY = paddle.getY() - fireball.getHeight()-10;

            fireball.setPosition(fireballX, fireballY); // Position above the paddle
        }
        
     // Check if fireball has fallen below the game area
        if (fireball.getY() >= HEIGHT) { // If the fireball is below the bottom edge
            decreaseChance(); // Decrease the player's chance
            return;
        }
       
        if (keys[KeyEvent.VK_SPACE] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        // Move the fireball if it's launched
        if (fireball.isLaunched()) {
            fireball.move(); // Update fireball's position if launched
        } else {
            // Keep fireball above the paddle if not launched
            fireball.setPosition(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        // Continuous movement logic for the paddle
        if (keys[KeyEvent.VK_LEFT]) {
            paddle.setDeltaX(-1, WIDTH); // Move paddle left
            paddle.setDirection(-1);
        }
        else if (keys[KeyEvent.VK_RIGHT]) {
            paddle.setDeltaX(1, WIDTH); // Move paddle right
            paddle.setDirection(1);
        }

        else{
            paddle.setDirection(0);
        }


        // Continuous rotation logic for the paddle
        if (keys[KeyEvent.VK_A]) {
            paddle.rotateAntiClockwise(deltaTime); // Rotate paddle anti-clockwise
        } else if (keys[KeyEvent.VK_D]) {
            paddle.rotateClockwise(deltaTime); // Rotate paddle clockwise
        } else {
            paddle.resetRotation(deltaTime); // Automatically rotate back to the horizontal position
        }

        // Check collision of fireball with walls
        fireball.checkCollisionWithWalls(WIDTH, HEIGHT);
        if ((currentTime - lastCollisionTime2) >= cooldownbar) {
        	fireball.checkCollisionWithBarriers(barriers);
        	lastCollisionTime2 = currentTime;
        }
        
        // Check collision of fireball with paddle and apply cooldown
        if (fireball.collidesWithPaddle(paddle) && (currentTime - lastCollisionTime) >= cooldown) {
            fireball.reflectFromPaddle(paddle); // Reflect fireball when colliding with paddle
            fireball.validateSpeed(paddle);
            lastCollisionTime = currentTime; // Update the last collision time
        }
        
        if (keys[KeyEvent.VK_SPACE] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
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
