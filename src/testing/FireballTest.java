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
// TEST CASE 1
    @Test
    public void testValidateSpeedPositiveDxPaddleLeft() {
        fireball.setVelocity(3, 3);
        paddle.setDirection(-1);
        fireball.validateSpeed(paddle);
        assertEquals(3, fireball.getVelocityX());
        assertEquals(3, fireball.getVelocityY());
    }
// TEST CASE 2
    @Test
    public void testValidateSpeedNegativeDxPaddleRight() {
        fireball.setVelocity(-3, 3);
        paddle.setDirection(1);
        fireball.validateSpeed(paddle);
        assertEquals(-3, fireball.getVelocityX());
        assertEquals(3, fireball.getVelocityY());
    }
// TEST CASE 3
    @Test
    public void testValidateSpeedZeroVelocity() {
        fireball.setVelocity(0, 0);
        paddle.setDirection(1);
        fireball.validateSpeed(paddle);
        assertEquals(0, fireball.getVelocityX());
        assertEquals(0, fireball.getVelocityY());
    }
// TEST CASE 4
    @Test
    public void testValidateSpeedZeroPaddleDirection() {
        fireball.setVelocity(3, 3);
        paddle.setDirection(0);
        fireball.validateSpeed(paddle);
        assertEquals(3, fireball.getVelocityX());
        assertEquals(3, fireball.getVelocityY());
    }
// TEST CASE 5
    @Test
    public void testValidateSpeedNullPaddle() {
        fireball.setVelocity(3, 3);
        assertThrows(NullPointerException.class, () -> {
            fireball.validateSpeed(null);
        });
    }
}
