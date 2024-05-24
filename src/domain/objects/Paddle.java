package domain.objects;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import domain.DEFAULT;
import domain.objects.Spells.Canons;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

public class Paddle {

    private BufferedImage image;
    private BufferedImage cannonImage;
    private int x;
    private int y;
    private int width=DEFAULT.paddleWidth;
    private int height=DEFAULT.paddleHeight;
    private int paddleSpeed = 6;
    private double rotationAngle = 0;
    private double rotationSpeed = 20; // degrees per second
    private int direction = 0;
    private boolean hexActive = false;
    private ArrayList<Canons> hexProjectiles = new ArrayList<>();

    public Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // Load the image from the class's resources
        try {
            image = ImageIO.read(getClass().getResource("/ui/images/Player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
     // Load the cannon image from the class's resources
        try {
            cannonImage = ImageIO.read(getClass().getResource("/ui/images/canon.png"));
            if (cannonImage == null) {
                System.err.println("Cannon image not found!");
            } else {
                System.out.println("Cannon image loaded successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
       
    }
    
    
    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public double getRotationAngle() {return rotationAngle;}
    public int getDirection() {return direction;}
    public void setDirection(int direction) {this.direction= direction;}

    
    public void setHexActive(boolean active) {
        this.hexActive = active;
        if (!active) {
            hexProjectiles.clear(); // Remove hex projectiles when deactivating
        }
        System.out.println("Hex active: " + hexActive); // Debug statement
    }

    public boolean isHexActive() {
        return hexActive;
    }
    
    public ArrayList<Canons> getHexProjectiles() {
        return hexProjectiles;
    }
    
    public void removeHexProjectile(Canons projectile) {
        hexProjectiles.remove(projectile);
    }
    
    
    public void setWidth(int newWidth) {
        this.width = newWidth;
        // Ensure the paddle remains within the game boundaries
        if (x + width > DEFAULT.screenWidth) {
            x = DEFAULT.screenWidth - width;
        }
        if (x < 0) {
            x = 0;
        }
    }
    
    
    // Responsible for left-right movement
    public void setDeltaX(int xDirection, int gameWidth) {
        int dx = xDirection * paddleSpeed;
        x += dx;
        
     // Constrain the paddle within the game boundaries
        if (x < 0) {
            x = 0; 
        } else if (x + width > gameWidth) {
            x = gameWidth - width;
        }
    }

    // Responsible for right rotation
    public void rotateClockwise(double deltaTime) {
        rotationAngle += rotationSpeed * deltaTime;
        if (rotationAngle > 45) {
            rotationAngle = 45;
        }
    }

    // Responsible for left rotation
    public void rotateAntiClockwise(double deltaTime) {
        rotationAngle -= rotationSpeed * deltaTime;
        if (rotationAngle < -45) {
            rotationAngle = -45;
        }
    }

    public void resetRotation(double deltaTime) {
        if (rotationAngle > 0) {
            rotationAngle -= rotationSpeed * deltaTime;
            if (rotationAngle < 0) rotationAngle = 0; // Prevent overshooting
        } else if (rotationAngle < 0) {
            rotationAngle += rotationSpeed * deltaTime;
            if (rotationAngle > 0) rotationAngle = 0;
        }
    }
    
    
    public void shootHex() {
        if (hexActive) {
            double radians = Math.toRadians(rotationAngle);
            int leftCannonX = x + width / 2 - (int) (Math.cos(radians) * width / 2);
            int leftCannonY = y + height / 2 - (int) (Math.sin(radians) * height / 2);
            int rightCannonX = x + width / 2 + (int) (Math.cos(radians) * width / 2);
            int rightCannonY = y + height / 2 + (int) (Math.sin(radians) * height / 2);

            hexProjectiles.add(new Canons(leftCannonX, leftCannonY, rotationAngle));
            hexProjectiles.add(new Canons(rightCannonX, rightCannonY, rotationAngle));
            System.out.println("Hex projectiles fired"); // Debug statement
        }
    }

    public void updateProjectiles() {
        for (Canons projectile : hexProjectiles) {
            projectile.move();
        }
        // Remove inactive projectiles
        hexProjectiles.removeIf(projectile -> !projectile.isActive());
    }
    

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(rotationAngle), x + width / 2, y + height / 2);
        g2d.drawImage(image, x, y, width, height, null);

        if (hexActive) {
            int cannonWidth = 10;
            int cannonHeight = 30;

            // Calculate the positions for the cannons
            int leftCannonX = x - cannonWidth / 2;
            int leftCannonY = y - cannonHeight;
            int rightCannonX = x + width - cannonWidth / 2;
            int rightCannonY = y - cannonHeight+10; 

            // Adjust the y-coordinate to place the cannons at the top of the paddle
            leftCannonY = y - cannonHeight/2;
            rightCannonY = y - cannonHeight/2;
            
         // Debug statements
            System.out.println("Left cannon position: (" + leftCannonX + ", " + leftCannonY + ")");
            System.out.println("Right cannon position: (" + rightCannonX + ", " + rightCannonY + ")");

            // Draw left cannon
            g2d.drawImage(cannonImage, leftCannonX, leftCannonY, cannonWidth, cannonHeight, null);
            // Draw right cannon
            g2d.drawImage(cannonImage, rightCannonX, rightCannonY, cannonWidth, cannonHeight, null);
        }

        for (Canons projectile : hexProjectiles) {
            projectile.draw(g2d);
        }
        g2d.dispose();
    }


 // To get the boundaries of the rotated paddle
    public Shape getBounds() {
        AffineTransform transform = new AffineTransform();
        transform.translate(x + width / 2, y + height / 2); // Translating transform object to the center of paddle
        transform.rotate(Math.toRadians(rotationAngle), 0, 0); // Rotate around the center
        Rectangle2D paddleRectangle = new Rectangle2D.Double(-width / 2, -height / 2, width, height); // Centered rectangle
        Shape transformedRect = transform.createTransformedShape(paddleRectangle); // Apply transformation to new rectangle
        return transformedRect;
    }
}