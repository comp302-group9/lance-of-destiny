package domain.objects.Barrier;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import domain.controllers.CollisionHandler;
import domain.objects.GameObject;
import domain.objects.Paddle;

public class Debris implements GameObject {
    private int x, y;
    private int width, height;
    private int fallSpeed;
    private int xSpeed; // Added horizontal speed
    private boolean isFalling;
    protected BufferedImage image;
    protected String img = "/ui/images/debris-explosion.png";
    private Random random = new Random();

    public Debris(int x, int y) {
        try {
            this.image = ImageIO.read(getClass().getResource(this.getImg()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.x = x;
        this.y = y;
        this.width = 20; // Arbitrary size, adjust as needed
        this.height = 20; // Arbitrary size, adjust as needed
        this.fallSpeed = 2; // Adjust speed as needed
        this.xSpeed = random.nextInt(3) - 1; // Random value between -1, 0, 1
        this.isFalling = true;
    }

    private String getImg() {
        return img;
    }

    public void update() {
        if (isFalling) {
            y += fallSpeed;
            x += xSpeed; // Update x position based on xSpeed
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    public int getY() {
        return y;
    }
    public boolean isFalling() {
        return isFalling;
    }

    public void stopFalling() {
        this.isFalling = false;
    }
    public boolean onHit() {
    	return true;
    }
    // Method to check collision with the paddle
    public void checkCollisionWithPaddle(Paddle paddle, ArrayList<Debris> debrisList, int screenHeight) {
        // Check if the debris collides with the paddle
        if (CollisionHandler.CollisionCheck(paddle, this)) {
            // Handle collision with the paddle
        	onHit();
            debrisList.remove(this);
        } else if (y > screenHeight) {
            // Remove debris if it exits the game window bounds
            debrisList.remove(this);
        }
    }
}