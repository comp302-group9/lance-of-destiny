package domain.objects.Spells;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import domain.DEFAULT;
import domain.objects.Barrier.Barrier;

public class Canons {
	
	 private int x, y;
	    private int width = 10, height = 20;
	    private double dx, dy; // Moving up by default
	    private double angle;
	    private BufferedImage image;
	    private boolean active = true;
	    
	    
	    public Canons(int startX, int startY, double angle) {
	        this.x = startX;
	        this.y = startY;
	        this.angle = angle;
	        this.dx = 5 * Math.sin(Math.toRadians(angle));
	        this.dy = -5 * Math.cos(Math.toRadians(angle));
	        try {
	            image = ImageIO.read(getClass().getResource("/ui/images/hexProjectile.png"));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        System.out.println("Hex projectile created at (" + x + ", " + y + ") with angle " + angle); // Debug statement
	    }
	    
	    public void move() {
	        if (!active) return;

	        x += dx;
	        y += dy;
	        
	        // Debug statement
	        System.out.println("Hex projectile moved to (" + x + ", " + y + ")");
	        
	        // Deactivate if it moves out of bounds
	        if (x < 0 || x > DEFAULT.screenWidth || y < 0 || y > DEFAULT.screenHeight) {
	            active = false;
	            System.out.println("Hex projectile deactivated due to out of bounds");
	        }
	    }


	    public void draw(Graphics g) {
	        if (!active) return;

	        Graphics2D g2d = (Graphics2D) g;
	        g2d.rotate(Math.toRadians(angle), x + (width / 2), y + (height / 2));
	        g2d.drawImage(image, x, y, width, height, null);
	        g2d.rotate(-Math.toRadians(angle), x + (width / 2), y + (height / 2));
	    }

	    public Rectangle getBounds() {
	        return new Rectangle(x, y, width, height);
	    }

	 // Implement collision detection with barriers
	    public boolean collidesWithBarrier(Barrier barrier) {
	        if (!active) return false;

	        Rectangle projectileBounds = getBounds();
	        Rectangle barrierBounds = barrier.getBounds();
	        return projectileBounds.intersects(barrierBounds);
	    }
	    
	    public boolean isActive() {
	        return active;
	    }

	 

}
