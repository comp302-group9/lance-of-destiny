package testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.objects.Paddle;

public class PaddleTest {
    private Paddle paddle;

    @BeforeEach
    public void setUp() {
        paddle = new Paddle(100, 100, 100, 20); // Initialize paddle with some default values
    }

// TEST CASE 1
    @Test
    public void testMoveRightWithinBoundaries() {
        paddle.setDeltaX(1, 800);
        assertEquals(106, paddle.getX());
    }
// TEST CASE 2
    @Test
    public void testMoveLeftWithinBoundaries() {
        paddle.setDeltaX(-1, 800);
        assertEquals(94, paddle.getX());
    }
// TEST CASE 3
    @Test
    public void testMoveRightAtRightBoundary() {
        paddle = new Paddle(700, 100, 100, 20); // Adjusted initial position for correct test
        paddle.setDeltaX(1, 800);
        assertEquals(700, paddle.getX());
    }
// TEST CASE 4
    @Test
    public void testMoveLeftAtLeftBoundary() {
        paddle = new Paddle(2, 100, 100, 20);
        paddle.setDeltaX(-1, 800);
        assertEquals(0, paddle.getX());
    }
// TEST CASE 5
    @Test
    public void testNoMovement() {
        paddle.setDeltaX(0, 800);
        assertEquals(100, paddle.getX());
    }
    
}
