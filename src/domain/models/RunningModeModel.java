package domain.models;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import database.DatabaseConnection;
import domain.DEFAULT;
import domain.controllers.CollisionHandler;
import domain.objects.Box;
import domain.objects.Fireball;
import domain.objects.ObjectFactory;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;
import domain.objects.Barrier.BarrierObserver;
import domain.objects.Barrier.Debris;
import domain.objects.Barrier.ExplosiveBarrier;
import domain.objects.Barrier.HollowPurpleBarrier;
import domain.objects.Barrier.ReinforcedBarrier;
import domain.objects.Barrier.RewardingBarrier;
import domain.objects.Barrier.SimpleBarrier;
import domain.objects.Spells.Canons;
import ui.screens.RModeUI.SpellIcon;

public class RunningModeModel implements BarrierObserver {
    public static int barrierWidth = 51;
    public static int barrierHeight = 15;
    private static final int ROWS = DEFAULT.ROWS;
    private static final int COLUMNS = DEFAULT.COLUMNS;
    public static final int HEIGHT = DEFAULT.screenHeight;
    public static final int WIDTH = DEFAULT.screenWidth;
    private Paddle paddle;
    private Fireball fireball;
    private User user;
    private long lastUpdateTime;
    private boolean paused = false;
    public static ArrayList<Barrier> barriers = new ArrayList<>();
    public static ArrayList<Box> boxes = new ArrayList<>();
    private ArrayList<HollowPurpleBarrier> purpleList = new ArrayList<>();
    private Random random = new Random();
    private boolean gameOver = false;
    private String gameOverMessage = "Game Over!";
    public static List<SpellIcon> spells = new ArrayList<>();
    private boolean gameStarted = false;
    private long lastHexShotTime = 0;
    private final long hexCooldown = 300;
    private ArrayList<Debris> debrisList= new ArrayList<>();

    private int[][] grid;
    private int gameId;

    private int chances = 3;

    private long lastCollisionTime = 0;
    private long lastCollisionTime2 = 0;
    private long cooldown = 1000;
    private long cooldownbar = 15;

    private int score = 0;
    private long gameStartingTime;

    private Runnable gameOverCallback;

    private Runnable scoreChangeCallback; // Add a callback for score change

    private BufferedReader in;  // To receive messages from the client or server
    private PrintWriter out;    // To send messages to the client or server
    private boolean isHost = false;
    private ConcurrentLinkedQueue<String> incomingMessages = new ConcurrentLinkedQueue<>();

    public RunningModeModel(User user, int[][] grid) {
        this.user =user;
        this.grid = grid;

        //setupQuitButtonListener();
        //setupSaveButtonListener();

        initializeGame();
        initializeSpellsSingle();

        // Start a separate thread to handle incoming messages
        new Thread(this::handleIncomingMessages).start();
//        }
        

        lastUpdateTime = System.currentTimeMillis();
        this.gameStartingTime = System.currentTimeMillis();
    }

    // CONSTRUCTOR FOR SAVED GAMES
    public RunningModeModel(User user, int[][] grid, int gameId) {
        
        this.user =user;
        this.grid = grid;
        this.gameId = gameId;

        initializeGame();
        initializeSpellsSingle();

        lastUpdateTime = System.currentTimeMillis();
        this.gameStartingTime = System.currentTimeMillis();
    }

    // CONSTRUCTOR FOR DUAL PLAYER MODE
    public RunningModeModel(User user, int[][] grid, BufferedReader in, PrintWriter out, boolean isHost) {
        this.user =user;
        this.grid = grid;
        this.in = in;
        this.out = out;
        this.isHost = isHost;

        initializeGame();
        
        boxes.add(new Box(WIDTH/2,300));

        // Initialize the paddle
        paddle = new Paddle(DEFAULT.screenWidth / 2, DEFAULT.screenHeight - 50, DEFAULT.paddleWidth, DEFAULT.paddleHeight); // Adjust parameters as needed
        
        // Initialize the fireball
        fireball = new Fireball( DEFAULT.screenWidth / 2, 7 * DEFAULT.screenHeight / 8, 16, 16); // Adjust parameters as needed
        if(spells.isEmpty()){
            initializeSpellsDual();

        initaliseBarrierLocations(grid);
        getFireball().setGrid(grid);;
        }
        

        lastUpdateTime = System.currentTimeMillis();
        this.gameStartingTime = System.currentTimeMillis();
    }

    public void initializeGame() {
        barriers.clear();
        paddle = new Paddle(WIDTH / 2, HEIGHT - 50, WIDTH / 10, 20);
        fireball = new Fireball(WIDTH / 2, 7 * HEIGHT / 8, 16, 16);
        lastUpdateTime = System.currentTimeMillis();
    }

    void initializeSpellsSingle() {
    	spells.clear();
        spells=ObjectFactory.getInstance().createSpellIcons2(this);
    }

    void initializeSpellsDual() {
    	spells.clear();
        spells=ObjectFactory.getInstance().createSpellIcons(this);
    }
    //
    public void setScore(int score) {
        this.score = score;
        System.out.println("Setting score: " + this.score);
    }

    // Method to set the number of chances
    public void setChances(int chances) {
        this.chances = chances;
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
    
    public ArrayList<Debris> getDebrisList(){
    	return this.debrisList;
    }

    public User getUser() {
        return user;
    }

    
    public int getGameId() {
        return gameId;
    }
    
    public ArrayList<HollowPurpleBarrier> getPurpleList(){
    	return this.purpleList;
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
    
    public void increaseChance() {
        chances++;
    }

    
    public void update(long currentTime, boolean[] keys) {
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0;
        lastUpdateTime = currentTime;


        /*/
        // Process incoming messages
        while (!incomingMessages.isEmpty()) {
            String message = incomingMessages.poll();
            if (message != null) {
                processMessage(message);
            }
        }
        */
        updateGameElements(deltaTime);
        handleCollisions(currentTime);
        
        if (fireball.getY() >= DEFAULT.screenHeight) {
            decreaseChance();
            return;
        }
        
        /*if (keys[KeyEvent.VK_W] && !fireball.isLaunched()) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        }
        */
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
        
        /*
        // Handle game start logic
        if (keys[KeyEvent.VK_SPACE] && !fireball.isLaunched()) {
            if (isHost) {
                fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
                out.println("LAUNCH_FIREBALL");  // Send launch message to the client
            } else {
                out.println("REQUEST_LAUNCH_FIREBALL");  // Send request to launch message to the host
            }
        } */

        if (keys[KeyEvent.VK_H] && paddle.isHexActive() && currentTime - lastHexShotTime >= hexCooldown) {
            paddle.shootHex();
            lastHexShotTime = currentTime;
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
        
        if (!debrisList.isEmpty()) {
            Iterator<Debris> iterator = debrisList.iterator();
            while (iterator.hasNext()) {
                Debris debris = iterator.next();
                debris.update();
                if (debris.getY() <  0) { 
                    iterator.remove();
                }
            }
        }
        
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            box.move();
            if (box.getY() > DEFAULT.screenHeight) {
                boxes.remove(i);
                i--;
            } else if (CollisionHandler.CollisionCheck(paddle, box)) {
                box.openBox();
                boxes.remove(i);
                i--;
                spells.get(random.nextInt(spells.size())).spell.increase();
            }
        }
    }
    
    private void handleCollisions(long currentTime) {
        CollisionHandler.checkWallCollision(fireball, DEFAULT.screenWidth, DEFAULT.screenHeight);
        
        if ((currentTime - lastCollisionTime2) >= cooldownbar) {
            fireball.checkCollisionWithBarriers(barriers, this);
            grid = fireball.getGrid();
            lastCollisionTime2 = currentTime;
        }
        
        if (CollisionHandler.CollisionCheck(paddle, fireball) && (currentTime - lastCollisionTime) >= cooldown) {
            fireball.reflectFromPaddle(paddle);
            fireball.validateSpeed(paddle);
            lastCollisionTime = currentTime;
        }
        
        if (!purpleList.isEmpty()) {
            Iterator<HollowPurpleBarrier> iterator = purpleList.iterator();
            while (iterator.hasNext()) {
            	HollowPurpleBarrier b = iterator.next();
            	if(CollisionHandler.CollisionCheck(fireball, b)) {
            		iterator.remove();
            	}
            }
        }
        
        if (!debrisList.isEmpty()) {
        	Iterator<Debris> iterator = debrisList.iterator();
        	while (iterator.hasNext()) {
        		Debris d = iterator.next();
        			if (CollisionHandler.CollisionCheck(d, paddle)) {
        				decreaseChance();
        				iterator.remove();
        			}	
        	}
        }
        
        
        for (Canons projectile : new ArrayList<>(paddle.getHexProjectiles())) {
            Iterator<Barrier> iterator = barriers.iterator();
            while (iterator.hasNext()) {
                Barrier barrier = iterator.next();
                if (CollisionHandler.CollisionCheck(projectile, barrier)) {
                    if (!barrier.getFrozen() && barrier.onHit()) {
                    	System.out.println("Cannon shoot: " + barrier.getGridX() + ", " + barrier.getGridY() );
                    	grid[barrier.getGridY()][barrier.getGridX()] = 0;
                        increaseScore(currentTime); // Increase score when a barrier is hit
                        iterator.remove();
                    }
                    paddle.removeHexProjectile(projectile);
                    break;
                }
            }
        }
    }
    private void handleIncomingMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                incomingMessages.add(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message) {
        if (message.equals("LAUNCH_FIREBALL")) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
        } else if (message.equals("REQUEST_LAUNCH_FIREBALL") && isHost) {
            fireball.launch(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - fireball.getHeight());
            out.println("LAUNCH_FIREBALL");  // Notify client to launch fireball
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
                    simple.setGridX(col);
                    simple.setGridY(row);
                    barriers.add(simple);
                    break;
                    case 2:
                    Barrier reinforced = new ReinforcedBarrier(x, y);
                    reinforced.setGridX(col);
                    reinforced.setGridY(row);
                    barriers.add(reinforced);
                    break;
                    case 3:
                    Barrier explosive = new ExplosiveBarrier(x, y);
                    explosive.setGridX(col);
                    explosive.setGridY(row);
                    barriers.add(explosive);
                    explosive.addObserver(this);
                    break;
                    case 4:
                    Barrier rewarding = new RewardingBarrier(x, y);
                    rewarding.setGridX(col);
                    rewarding.setGridY(row);
                    barriers.add(rewarding);
                    break;
                }
            }
        }
    }
    
    public void onDebrisCreated(ArrayList<Debris> newDebris) {
        debrisList.addAll(newDebris);
    }
    
    
    public void saveGame(int[][] matrix, int gameId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE SavedGames SET grid = ?, score = ?, life = ? WHERE gameId = ?";
            pstmt = conn.prepareStatement(sql);
            
            //            // Convert the 2D array into a single string
            //            StringBuilder gridBuilder = new StringBuilder();
            //            for (int i = 0; i < matrix.length; i++) {
                //                for (int j = 0; j < matrix[i].length; j++) {
                    //                    gridBuilder.append(matrix[i][j]).append(" ");
                    //                }
//            }
//            String gridString = gridBuilder.toString().trim(); // Remove trailing space

            String gridString = writeGrid(matrix);

            pstmt.setString(1, gridString);
            pstmt.setInt(2, score);  // Set score
            pstmt.setInt(3, chances);  // Set chances
            pstmt.setInt(4, gameId); 

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating the grid failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
    }

    public String writeGrid(int[][] matrix) {
        StringBuilder gridStringBuilder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                gridStringBuilder.append(matrix[i][j]).append(" ");
            }
        }
        String gridString = gridStringBuilder.toString().trim(); // Remove trailing space
        return gridString;
                
    }

    public Paddle getPaddle() {return paddle;}
    public Fireball getFireball() {return fireball;}
    
    public void addPurpleBarrier(HollowPurpleBarrier barrier) {
		// TODO Auto-generated method stub
		//assumes barriers only placed with respect to grid.
    	grid[barrier.getGridX()][barrier.getGridY()]= 1;
		purpleList.add(barrier);

	}


	public void removePurpleBarrier(Barrier barrier) {
		grid[barrier.getGridX()][barrier.getGridY()]= 0;
		purpleList.remove(barrier);

	}
}
    