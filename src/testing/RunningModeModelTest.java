package testing;
import domain.models.RunningModeModel;
import domain.models.BuildingModeModel;
import ui.screens.BuildingModeView;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;
import domain.objects.Barrier.SimpleBarrier;
import domain.objects.Box;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class RunningModeModelTest {

    private RunningModeModel model;
    private boolean[] keys;

    @BeforeEach
    void setUp() {
        model = new RunningModeModel();
        keys = new boolean[256]; 
    }
    
    // BEHAVIOR SPECIFICATION FOR UPDATE METHOD
    /**
     * Requires:
     * - currentTime is the current system time in milliseconds.
     * - keys is a boolean array representing the state of keyboard keys (true if pressed, false otherwise).
     * 
     * Modifies:
     * - The position and state of the fireball.
     * - The position, direction, and rotation of the paddle.
     * - The position and state of the barriers..
     * - The game's paused and game-over states.
     * 
     * Effects:
     * - Updates the positions and states of the paddle, fireball, barriers, and boxes based on the time elapsed and keyboard inputs.
     * - Reflects the fireball if it collides with the paddle.
     * - Ends the game if the fireball falls below the game area.
     * - Moves the paddle and fireball based on keyboard inputs.
     * - Aligns the fireball to the paddle if not launched.
     * - Moves the barriers and boxes.
     * - Resets the fireball's position above the paddle if not launched.
     */

    @Test
    void testInitialFireballPositioning() { // Black Box
        //long initialTime = System.currentTimeMillis();
        //model.update(initialTime, keys);
        Fireball fireball = model.getFireball();
        Paddle paddle = model.getPaddle();
        System.out.println(paddle.getX());
        System.out.println(fireball.getX());
        assertEquals(paddle.getY() - 25, fireball.getY());
        assertEquals(paddle.getX(), fireball.getX()); 
        
    }

    @Test
    void testFireballLaunch() { // Black Box
        keys[KeyEvent.VK_SPACE] = true;
        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);
        Fireball fireball = model.getFireball();

        assertTrue(fireball.isLaunched());
    }
    
    @Test
    void testBoxMovement() { // Black Box
        Box box = new Box(100, RunningModeModel.HEIGHT + 1); // Position box below the screen
        model.boxes.add(box);

        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);

        assertFalse(model.boxes.contains(box)); // Box should be removed from the game area
    }

    
    @Test
    void testFireballCollisionWithPaddle() { // Glass Box
        Fireball fireball = model.getFireball();
        Paddle paddle = model.getPaddle();
        fireball.setPosition(paddle.getX(), paddle.getY());

        assertTrue(fireball.collidesWithPaddle(paddle)); // Fireball should collide with the paddle
    }

    @Test
    void testBarrierMovement() { // Glass Box
        Barrier movingBarrier = new SimpleBarrier(100, 100);
        movingBarrier.isMoving = true;
        model.barriers.add(movingBarrier);

        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);
        double initialX = movingBarrier.getX();

        model.update(initialTime + 100, keys);
        assertNotEquals(initialX, movingBarrier.getX()); // Barrier should have moved
    }


    
    @Test
    void testFireballCollisionWithLeftWall() { // Glass Box
        Fireball fireball = model.getFireball();
        fireball.setPosition(0, fireball.getY()); // Position fireball at the left wall
        fireball.setVelocity(-1, 0); // Moving towards the wall

        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);

        assertTrue(fireball.getVelocityX() < 0); // Velocity should reflect
    }

   
}