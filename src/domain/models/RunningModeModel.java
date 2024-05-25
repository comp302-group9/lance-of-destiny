package domain.models;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
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
import domain.objects.Spells.Canons;
import domain.objects.Spells.Expension;
import domain.objects.Spells.Hex;
import domain.objects.Spells.Overwhelm;
import ui.screens.RModeUI.SpellIcon;

public class RunningModeModel {
    public static int barrierWidth = 51;
    public static int barrierHeight = 15;
    private static final int ROWS = DEFAULT.ROWS;
    private static final int COLUMNS = DEFAULT.COLUMNS;
    public static final int HEIGHT = DEFAULT.screenHeight;
    public static final int WIDTH = DEFAULT.screenWidth;
    private Paddle paddle;
    private Fireball fireball;
    private long lastUpdateTime;
    private boolean paused = false;
    public static ArrayList<Barrier> barriers = new ArrayList<>();
    public static ArrayList<Box> boxes = new ArrayList<>();
    private Random random = new Random();
    private boolean gameOver = false;
    private String gameOverMessage = "Game Over!";
    public static ArrayList<SpellIcon> spells = new ArrayList<>();
    private boolean gameStarted = false;
    private long lastHexShotTime = 0;
    private final long hexCooldown = 300;

    private int[][] grid;

    private int chances = 3;

    private long lastCollisionTime = 0;
    private long lastCollisionTime2 = 0;
    private long cooldown = 1000;
    private long cooldownbar = 15;

    private int score = 0;
    private long gameStartingTime;

    private Runnable gameOverCallback;

    private Runnable scoreChangeCallback; // Add a callback for score change

    public RunningModeModel() {
        initializeGame();
        boxes.add(new Box(WIDTH / 2, 300));

        paddle = new Paddle(DEFAULT.screenWidth / 2, DEFAULT.screenHeight - 50, DEFAULT.paddleWidth, DEFAULT.paddleHeight);
        spells.add(new SpellIcon(new Hex(paddle)));
        spells.add(new SpellIcon(new Expension(paddle)));

        fireball = new Fireball(DEFAULT.screenWidth / 2, 7 * DEFAULT.screenHeight / 8, 16, 16);
        spells.add(new SpellIcon(new Overwhelm(fireball)));

        lastUpdateTime = System.currentTimeMillis();
        this.gameStartingTime = System.currentTimeMillis();
    }

    private void initializeGame() {
        barriers.clear();
        paddle = new Paddle(WIDTH / 2, HEIGHT - 50, WIDTH / 10, 20);
        fireball = new Fireball(WIDTH / 2, 7 * HEIGHT / 8, 16, 16);
        lastUpdateTime = System.currentTimeMillis();
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(long currentTime) {
        score += 300 / ((currentTime - gameStartingTime) / 1000.0);
        if (scoreChangeCallback != null) {
            scoreChangeCallback.run();
        }
    }

    public void setScoreChangeCallback(Runnable callback) {
        this.scoreChangeCallback = callback;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGameOverCallback(Runnable gameOverCallback) {
        this.gameOverCallback = gameOverCallback;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public void setFireball(Fireball fireball) {
        this.fireball = fireball;
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
        fireball.setPosition(100, 100);
        fireball.setPosition(paddle.getX() + paddle.getWidth() / 2 - fireball.getWidth() / 2, paddle.getY() - fireball.getHeight());
        fireball.setLaunched(false);
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

    public Paddle getPaddle() {
        return paddle;
    }

    public Fireball getFireball() {
        return fireball;
    }

    public void update(long currentTime, boolean[] keys) {
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0;
        lastUpdateTime = currentTime;

        updateGameElements(deltaTime);
        handleCollisions(currentTime);

        if (fireball.getY() >= DEFAULT.screenHeight) {
            decreaseChance();
            return;
        }

        if (keys[KeyEvent.VK_W] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        if (keys[KeyEvent.VK_LEFT]) {
            paddle.setDeltaX(-1, DEFAULT.screenWidth);
            paddle.setDirection(-1);
        } else if (keys[KeyEvent.VK_RIGHT]) {
            paddle.setDeltaX(1, DEFAULT.screenWidth);
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

        if (keys[KeyEvent.VK_SPACE] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        if (keys[KeyEvent.VK_H] && paddle.isHexActive() && currentTime - lastHexShotTime >= hexCooldown) {
            paddle.shootHex();
            lastHexShotTime = currentTime;
            System.out.println("Hex fired");
        }

        paddle.updateProjectiles();
    }

    private void updateGameElements(double deltaTime) {
        if (fireball.isLaunched()) {
            fireball.move();
        } else {
            fireball.setPosition(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }

        for (Barrier barrier : barriers) {
            if (barrier.isMoving) {
                barrier.move(barriers, deltaTime);
            }
        }

        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            box.move();
            if (box.getY() > DEFAULT.screenHeight) {
                boxes.remove(i);
                i--;
            } else if (box.collidesWithPaddle(paddle)) {
                box.openBox();
                boxes.remove(i);
                i--;
                spells.get(random.nextInt(spells.size())).spell.increase();
            }
        }
    }

    private void handleCollisions(long currentTime) {
        fireball.checkCollisionWithWalls(DEFAULT.screenWidth, DEFAULT.screenHeight);
        if ((currentTime - lastCollisionTime2) >= cooldownbar) {
            fireball.checkCollisionWithBarriers(barriers, this);
            grid = fireball.getGrid();
            lastCollisionTime2 = currentTime;
        }

        if (fireball.collidesWithPaddle(paddle) && (currentTime - lastCollisionTime) >= cooldown) {
            fireball.reflectFromPaddle(paddle);
            fireball.validateSpeed(paddle);
            lastCollisionTime = currentTime;
        }

        for (Canons projectile : new ArrayList<>(paddle.getHexProjectiles())) {
            Iterator<Barrier> iterator = barriers.iterator();
            while (iterator.hasNext()) {
                Barrier barrier = iterator.next();
                if (projectile.collidesWithBarrier(barrier)) {
                    if (!barrier.getFrozen() && barrier.onHit()) {
                        increaseScore(currentTime); // Increase score when a barrier is hit
                        iterator.remove();
                    }
                    paddle.removeHexProjectile(projectile);
                    System.out.println("Hex projectile collided with barrier");
                    break;
                }
            }
        }
    }

    public void initaliseBarrierLocations(int[][] grid) {
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
                        Barrier simple = new SimpleBarrier(x, y);
                        barriers.add(simple);
                        break;
                    case 2:
                        Barrier reinforced = new ReinforcedBarrier(x, y);
                        barriers.add(reinforced);
                        break;
                    case 3:
                        Barrier explosive = new ExplosiveBarrier(x, y);
                        barriers.add(explosive);
                        break;
                    case 4:
                        Barrier rewarding = new RewardingBarrier(x, y);
                        barriers.add(rewarding);
                        break;
                }
            }
        }
    }
}
