import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import domain.objects.Fireball;
import domain.objects.Paddle;


import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class ValidateSpeedTest {
	
	private Fireball fireball;
    private Paddle paddle;

    @BeforeEach
    public void setUp() {
        fireball = new Fireball(0, 0, 10, 10);
        paddle = new Paddle(0, 0, 50, 10); // Assuming Paddle has a similar constructor
    }

    @Test
    public void testValidateSpeed_IncreaseSpeedWhenMovingSameDirection() {
        fireball.setVelocity(3, 4);
        paddle.setDirection(1); // Assume this method exists to set direction of paddle

        fireball.validateSpeed(paddle);

        double newDx = fireball.getDx();
        double newDy = fireball.getDy();

        // The speed should have increased
        assertTrue(newDx > 3);
        assertTrue(newDy > 4);
    }

    @Test
    public void testValidateSpeed_NoChangeWhenMovingOppositeDirection() {
        fireball.setVelocity(3, 4);
        paddle.setDirection(-1); // Paddle moving in opposite direction

        fireball.validateSpeed(paddle);

        double newDx = fireball.getDx();
        double newDy = fireball.getDy();

        // The speed should not have changed
        assertEquals(3, newDx, 0.0001);
        assertEquals(4, newDy, 0.0001);
    }

    @Test
    public void testValidateSpeed_IncreaseSpeedWhenMovingNegativeDirection() {
        fireball.setVelocity(-3, -4);
        paddle.setDirection(-1); // Paddle moving in the same negative direction

        fireball.validateSpeed(paddle);

        double newDx = fireball.getDx();
        double newDy = fireball.getDy();

        // The speed should have increased (in the negative direction)
        assertTrue(newDx < -3);
        assertTrue(newDy < -4);
    }

    @Test
    public void testValidateSpeed_NoChangeWhenMovingOppositeNegativeDirection() {
        fireball.setVelocity(-3, -4);
        paddle.setDirection(1); // Paddle moving in the opposite direction

        fireball.validateSpeed(paddle);

        double newDx = fireball.getDx();
        double newDy = fireball.getDy();

        // The speed should not have changed
        assertEquals(-3, newDx, 0.0001);
        assertEquals(-4, newDy, 0.0001);
    }

    @Test
    public void testValidateSpeed_SpeedIncreasesProportionally() {
        fireball.setVelocity(5, 12); // Speed is 13 (5-12-13 triangle)
        paddle.setDirection(1); // Paddle moving in the same direction

        fireball.validateSpeed(paddle);

        double newDx = fireball.getDx();
        double newDy = fireball.getDy();

        // The new speed should be 13 + (5/64)
        double expectedSpeed = 13 + (5 / 64.0);
        double newSpeed = Math.sqrt(newDx * newDx + newDy * newDy);

        assertEquals(expectedSpeed, newSpeed, 0.0001);
    }
	
	
   
	
	
    
    
    
    
    

    

}


