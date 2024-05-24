package domain.objects.Barrier;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import domain.objects.Paddle;//parameter visibility

public class Debris {
    private int x, y;
    private int width, height;
    private int fallSpeed;
    private boolean isFalling;
   
    public Debris(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 10; // Arbitrary size, adjust as needed
        this.height = 10; // Arbitrary size, adjust as needed
        this.fallSpeed = 5; // Adjust speed as needed
        this.isFalling = true;
    }

    public void update() {
        if (isFalling) {
        	
            y += fallSpeed;
        }
    }

    public void draw(Graphics g) {
        g.fillRect(x, y, width, height); // Simple rectangle for now, replace with image if needed
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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
        if (paddle.getBounds().intersects(getBounds())) {
            // Handle collision with the paddle
        	onHit();
            debrisList.remove(this);
        } else if (y > screenHeight) {
            // Remove debris if it exits the game window bounds
            debrisList.remove(this);
        }
    }
}
