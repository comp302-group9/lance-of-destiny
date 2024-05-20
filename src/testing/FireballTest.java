package testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.objects.Fireball;
import domain.objects.Paddle;

public class FireballTest {

    private Fireball fireball;
    private Paddle paddle;

    @BeforeEach
    public void setUp() {
        fireball = new Fireball(0, 0, 10, 10);
        paddle = new Paddle(0, 0, 0, 0);
    }

    @Test
    public void testValidateSpeedPositiveDxPaddleRight() {
        fireball.setVelocity(3, 3);
        paddle.setDirection(1);
        fireball.validateSpeed(paddle);
        assertTrue(fireball.getVelocityX() > 3);
        assertTrue(fireball.getVelocityY() > 3);
    }

    @Test
    public void testValidateSpeedNegativeDxPaddleLeft() {
        fireball.setVelocity(-3, 3);
        paddle.setDirection(-1);
        fireball.validateSpeed(paddle);
        assertTrue(fireball.getVelocityX() < -3);
        assertTrue(fireball.getVelocityY() > 3);
    }

    @Test
    public void testValidateSpeedPositiveDxPaddleLeft() {
        fireball.setVelocity(3, 3);
        paddle.setDirection(-1);
        fireball.validateSpeed(paddle);
        assertEquals(3, fireball.getVelocityX());
        assertEquals(3, fireball.getVelocityY());
    }

    @Test
    public void testValidateSpeedNegativeDxPaddleRight() {
        fireball.setVelocity(-3, 3);
        paddle.setDirection(1);
        fireball.validateSpeed(paddle);
        assertEquals(-3, fireball.getVelocityX());
        assertEquals(3, fireball.getVelocityY());
    }

    @Test
    public void testValidateSpeedZeroVelocity() {
        fireball.setVelocity(0, 0);
        paddle.setDirection(1);
        fireball.validateSpeed(paddle);
        assertEquals(0, fireball.getVelocityX());
        assertEquals(0, fireball.getVelocityY());
    }

    @Test
    public void testValidateSpeedBoundarySmallPositiveDx() {
        fireball.setVelocity(0.001, 0.001);
        paddle.setDirection(1);
        fireball.validateSpeed(paddle);
        assertTrue(fireball.getVelocityX() > 0.001);
        assertTrue(fireball.getVelocityY() > 0.001);
    }

    @Test
    public void testValidateSpeedBoundarySmallNegativeDx() {
        fireball.setVelocity(-0.001, 0.001);
        paddle.setDirection(-1);
        fireball.validateSpeed(paddle);
        assertTrue(fireball.getVelocityX() < -0.001);
        assertTrue(fireball.getVelocityY() > 0.001);
    }

    @Test
    public void testValidateSpeedLargeVelocity() {
        fireball.setVelocity(1000, 1000);
        paddle.setDirection(1);
        fireball.validateSpeed(paddle);
        assertTrue(fireball.getVelocityX() > 1000);
        assertTrue(fireball.getVelocityY() > 1000);
    }

    @Test
    public void testValidateSpeedZeroPaddleDirection() {
        fireball.setVelocity(3, 3);
        paddle.setDirection(0);
        fireball.validateSpeed(paddle);
        assertEquals(3, fireball.getVelocityX());
        assertEquals(3, fireball.getVelocityY());
    }

    @Test
    public void testValidateSpeedNullPaddle() {
        fireball.setVelocity(3, 3);
        assertThrows(NullPointerException.class, () -> {
            fireball.validateSpeed(null);
        });
    }
}
