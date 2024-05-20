package domain.models;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import domain.DEFAULT;
import domain.objects.Box;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;
import domain.objects.Barrier.ExplosiveBarrier;
import domain.objects.Barrier.ReinforcedBarrier;
import domain.objects.Barrier.RewardingBarrier;
import domain.objects.Barrier.SimpleBarrier;
import ui.screens.RModeUI.SpellIcon;
import domain.objects.Spells.Expension;
import domain.objects.Spells.Hex;
import domain.objects.Spells.Overwhelming;

public class RunningModeModel{
    public static int barrierWidth = 51;
	public static int barrierHeight =15;
    private static final int ROWS = DEFAULT.ROWS;
    private static final int COLUMNS = DEFAULT.COLUMNS;
    private Paddle paddle;
    private Fireball fireball;
    private long lastUpdateTime;
    private boolean paused = false; 
    public static ArrayList<Barrier> barriers = new ArrayList<Barrier>();
    public static ArrayList<Box> boxes= new ArrayList<Box>();
    private Random random=new Random();
    private boolean gameOver = false; // State to track if the game is over
    private String gameOverMessage = "Game Over!"; // Game over message
    public static ArrayList<SpellIcon> spells= new ArrayList<SpellIcon>();
    private boolean gameStarted = false;

    public RunningModeModel() {
    	spells.add(new SpellIcon(new Expension()));
        spells.add(new SpellIcon(new Hex()));
        spells.add(new SpellIcon(new Overwhelming(false)));
        //spells.add(new SpellIcon("src\\ui\\images\\Hex.png"));
        //spells.add(new SpellIcon("src\\ui\\images\\extend.png"));

        // Initialize the paddle
        paddle = new Paddle(DEFAULT.screenWidth / 2, DEFAULT.screenHeight - 50, DEFAULT.paddleWidth, DEFAULT.paddleHeight); // Adjust parameters as needed

        // Initialize the fireball
        fireball = new Fireball( DEFAULT.screenWidth / 2, 7 * DEFAULT.screenHeight / 8, 16, 16); // Adjust parameters as needed

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
        fireball.setPosition(100, 100);
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
    long cooldownbar = 25;

    //APPLICATION OF OBSERVER PATTERN 
    // When the RunningModeModel changes state, 
    // it calls a method such as notifyObservers() in our case update() method
    // to loop through all registered observers (paddle, fireball, barrier and boxes) and call their own update methods, like paddle.update(this).
    // We know that observer pattern needs to be implemented using an Interface and we will change our code to do so
    // but this is the part that we will apply the observer pattern

    public void update(long currentTime, boolean[] keys) {
    	double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // Convert to seconds
        lastUpdateTime = currentTime;
        
       updateGameElements(deltaTime);
       handleCollisions(currentTime);
        
     // Check if fireball has fallen below the game area
        if (fireball.getY() >= DEFAULT.screenHeight) { // If the fireball is below the bottom edge
            setGameOver(true); // Set the game-over state
            return;
        }
       
        if (keys[KeyEvent.VK_W] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        // Continuous movement logic for the paddle
        if (keys[KeyEvent.VK_LEFT]) {
            paddle.setDeltaX(-1, DEFAULT.screenWidth); // Move paddle left
            paddle.setDirection(-1);
        }
        else if (keys[KeyEvent.VK_RIGHT]) {
            paddle.setDeltaX(1, DEFAULT.screenWidth); // Move paddle right
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

        if (keys[KeyEvent.VK_SPACE] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }
    }

    private void updateGameElements(double deltaTime) {
        if (fireball.isLaunched()) {
            fireball.move(); // Update fireball's position if launched
        } else {
            // Keep fireball above the paddle if not launched
            fireball.setPosition(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        for (Barrier barrier : barriers) {
            if (barrier.isMoving) {
                barrier.move(barriers, deltaTime); // Move barrier and check for collisions
            }
        }
        
        for (int i=0; i<boxes.size() ; i++){
            Box box = boxes.get(i);
            box.move();
            if (box.getY() > DEFAULT.screenHeight) {
                boxes.remove(i);
                i--;
            }
        }
    }

    private void handleCollisions(long currentTime) {
        // Check collision of fireball with walls
        fireball.checkCollisionWithWalls(DEFAULT.screenWidth, DEFAULT.screenHeight);
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
    }

    public void initaliseBarrierLocations(int[][] grid){
        int xStart = DEFAULT.screenHeight / 32;
		int yStart = DEFAULT.screenWidth / 32;
		int xGap = DEFAULT.screenHeight / 128;
		int yGap = DEFAULT.screenHeight / 96;
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