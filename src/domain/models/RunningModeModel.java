package domain.models;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import domain.objects.Box;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;
import domain.objects.ObjectFactory;
import ui.screens.BuildingModeView;

public class RunningModeModel {
    public static final int WIDTH = BuildingModeView.WIDTH;
    public static final int HEIGHT = BuildingModeView.HEIGHT;
    public static int barrierWidth = 51;
    public static int barrierHeight = 15;
    private static final int ROWS = BuildingModeModel.ROWS;
    private static final int COLUMNS = BuildingModeModel.COLUMNS;
    private Paddle paddle;
    private Fireball fireball;
    private long lastUpdateTime;
    private boolean paused = false; 
    public static ArrayList<Barrier> barriers = new ArrayList<>();
    public static ArrayList<Box> boxes = new ArrayList<>();
    private Random random = new Random();
    private boolean gameOver = false;
    private String gameOverMessage = "Game Over!";
    private int chances = 3;
    private Runnable gameOverCallback;
    private int score = 0;
    private long gameStartingTime;

    private long lastCollisionTime = 0;
    private long lastCollisionTime2 = 0;
    private long cooldown = 1000;
    private long cooldownbar = 15;

    public RunningModeModel() {
        ObjectFactory factory = ObjectFactory.getInstance();
        paddle = factory.createPaddle(WIDTH / 2, HEIGHT - 50, WIDTH / 10, 20);
        fireball = factory.createFireball(WIDTH / 2, 7 * HEIGHT / 8, 16, 16);
        lastUpdateTime = System.currentTimeMillis();
        this.gameStartingTime = System.currentTimeMillis();
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(long currentTime) {
        score += 300 / ((currentTime - gameStartingTime) / 1000.0);
    }

    public void setGameOverCallback(Runnable gameOverCallback) {
        this.gameOverCallback = gameOverCallback;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public String gameOverMessage() {
        return gameOverMessage;
    }
    
    public void restart() {
        setGameOver(false);
        fireball.setPosition(paddle.getX() + paddle.getWidth() / 2 - fireball.getWidth() / 2, paddle.getY() - fireball.getHeight());
        fireball.setLaunched(false);
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
            if (gameOverCallback != null) {
                gameOverCallback.run();
            }
        } else {
            restart();
        }
    }

    public void update(long currentTime, boolean[] keys) {
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0;
        lastUpdateTime = currentTime;

        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            box.move();
            if (box.getY() > HEIGHT) {
                boxes.remove(i);
                i--;
            }
        }

        for (Barrier barrier : barriers) {
            if (barrier.isMoving) {
                barrier.move(barriers, deltaTime);
            }
        }

        if (!fireball.isLaunched()) {
            int fireballX = paddle.getX() + (paddle.getWidth() - fireball.getWidth()) / 2;
            int fireballY = paddle.getY() - fireball.getHeight() - 10;
            fireball.setPosition(fireballX, fireballY);
        }

        if (fireball.getY() >= HEIGHT) {
            decreaseChance();
            return;
        }

        if (keys[KeyEvent.VK_SPACE] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        if (fireball.isLaunched()) {
            fireball.move();
        } else {
            fireball.setPosition(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        if (keys[KeyEvent.VK_LEFT]) {
            paddle.setDeltaX(-1, WIDTH);
            paddle.setDirection(-1);
        } else if (keys[KeyEvent.VK_RIGHT]) {
            paddle.setDeltaX(1, WIDTH);
            paddle.setDirection(1);
        } else {
            paddle.setDirection(0);
        }

        if (keys[KeyEvent.VK_A]) {
            paddle.rotateAntiClockwise(deltaTime);
        } else if (keys[KeyEvent.VK_D]) {
            paddle.rotateClockwise(deltaTime);
        } else {
            paddle.resetRotation(deltaTime);
        }

        fireball.checkCollisionWithWalls(WIDTH, HEIGHT);
        if ((currentTime - lastCollisionTime2) >= cooldownbar) {
            fireball.checkCollisionWithBarriers(barriers, this);
            lastCollisionTime2 = currentTime;
        }

        if (fireball.collidesWithPaddle(paddle) && (currentTime - lastCollisionTime) >= cooldown) {
            fireball.reflectFromPaddle(paddle);
            fireball.validateSpeed(paddle);
            lastCollisionTime = currentTime;
        }

        if (keys[KeyEvent.VK_SPACE] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        fireball.checkCollisionWithBarriers(barriers, this);
    }

    public void initaliseBarrierLocations(int[][] grid) {
        ObjectFactory factory = ObjectFactory.getInstance();
        int xStart = HEIGHT / 32;
        int yStart = WIDTH / 32;
        int xGap = HEIGHT / 128;
        int yGap = WIDTH / 96;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int x = xStart + col * (barrierWidth + xGap);
                int y = yStart + row * (barrierHeight + yGap);
                Barrier barrier = factory.createBarrier(grid[row][col], x, y);
                if (barrier != null) {
                    barriers.add(barrier);
                }
            }
        }
    }
}
