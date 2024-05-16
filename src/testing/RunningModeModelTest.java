package testing;
import domain.models.RunningModeModel;
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

    @Test
    void testInitialFireballPositioning() {
        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);
        Fireball fireball = model.getFireball();
        Paddle paddle = model.getPaddle();

        assertEquals(paddle.getX() + (paddle.getWidth() - fireball.getWidth()) / 2, fireball.getX());
        assertEquals(paddle.getY() - fireball.getHeight() - 10, fireball.getY());
    }

    @Test
    void testFireballLaunch() {
        keys[KeyEvent.VK_SPACE] = true;
        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);
        Fireball fireball = model.getFireball();

        assertTrue(fireball.isLaunched());
    }

    @Test
    void testGameOverCondition() {
        Fireball fireball = model.getFireball();
        fireball.setPosition(0, RunningModeModel.HEIGHT + 1); // Below the screen
        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);

        assertTrue(model.isGameOver());
    }

    @Test
    void testPaddleMovement() {
        Paddle paddle = model.getPaddle();
        keys[KeyEvent.VK_LEFT] = true;
        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);
        assertTrue(paddle.getX() < RunningModeModel.WIDTH / 2); // Paddle moved left

        keys[KeyEvent.VK_LEFT] = false;
        keys[KeyEvent.VK_RIGHT] = true;
        model.update(initialTime + 100, keys); // Adding deltaTime to simulate time passing
        assertTrue(paddle.getX() > RunningModeModel.WIDTH / 2); // Paddle moved right
    }

    @Test
    void testFireballCollisionWithPaddle() {
        Fireball fireball = model.getFireball();
        Paddle paddle = model.getPaddle();
        fireball.setPosition(paddle.getX(), paddle.getY() - fireball.getHeight());

        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);

        assertTrue(fireball.collidesWithPaddle(paddle)); // Fireball should collide with the paddle
    }

    @Test
    void testBarrierMovement() {
        Barrier movingBarrier = new SimpleBarrier(100, 100);
        movingBarrier.isMoving = true;
        model.barriers.add(movingBarrier);

        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);
        double initialX = movingBarrier.getX();

        model.update(initialTime + 100, keys); // Adding deltaTime to simulate time passing
        assertNotEquals(initialX, movingBarrier.getX()); // Barrier should have moved
    }

    @Test
    void testCooldownMechanism() {
        Fireball fireball = model.getFireball();
        Paddle paddle = model.getPaddle();
        fireball.setPosition(paddle.getX(), paddle.getY() - fireball.getHeight());

        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys); // First collision

        long afterCooldown = initialTime + 1500; // Time after cooldown
        fireball.setPosition(paddle.getX(), paddle.getY() - fireball.getHeight()); // Reset fireball position
        model.update(afterCooldown, keys);

        assertTrue(fireball.collidesWithPaddle(paddle)); // Should collide again after cooldown
    }

    @Test
    void testPaddleRotation() {
        keys[KeyEvent.VK_A] = true; // Rotate anti-clockwise
        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);
        Paddle paddle = model.getPaddle();

        assertNotEquals(0, paddle.getRotationAngle()); // Paddle should have rotated

        keys[KeyEvent.VK_A] = false;
        keys[KeyEvent.VK_D] = true; // Rotate clockwise
        model.update(initialTime + 100, keys); // Adding deltaTime to simulate time passing

        assertNotEquals(0, paddle.getRotationAngle()); // Paddle should have rotated
    }

    @Test
    void testFireballCollisionWithWalls() {
        Fireball fireball = model.getFireball();
        fireball.setPosition(0, fireball.getY()); // Position fireball at the left wall
        fireball.setVelocity(-1, 0); // Moving towards the wall

        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);

        assertTrue(fireball.getVelocityX() > 0); // Velocity should reflect, making it positive
    }

    @Test
    void testBoxMovement() {
        Box box = new Box(100, RunningModeModel.HEIGHT + 1); // Position box below the screen
        model.boxes.add(box);

        long initialTime = System.currentTimeMillis();
        model.update(initialTime, keys);

        assertFalse(model.boxes.contains(box)); // Box should be removed from the game area
    }
}