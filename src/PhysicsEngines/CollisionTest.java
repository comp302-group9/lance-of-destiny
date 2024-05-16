package PhysicsEngines;
import domain.objects.*;
import domain.objects.Barrier.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CollisionTest {

	@Test
	void test() {
		
		testReflect();
		testFireballBarrierCollision();
		
		fail("Not yet implemented");
	}
	public void testReflect() {
        
        Fireball fireball = new Fireball(533,325,16,16);
        Paddle paddle = new Paddle(533, 550, 107, 20);

        // Test reflection when moving in the same direction as object movement
        Vector collisionNormal = new Vector(1, 0); // Assume collision normal is perpendicular to movement direction
        Vector objectMovementDirection = new Vector(1, 0); // Assume paddle is moving to the right
        fireball.reflect(collisionNormal, objectMovementDirection);
        Vector expectedVelocity = new Vector(-1, 1).add(objectMovementDirection.normalize().multiply(5));
        assertEquals(expectedVelocity.getX(), fireball.getVelocity().getX(), 0.001);
        assertEquals(expectedVelocity.getY(), fireball.getVelocity().getY(), 0.001);

        // Test reflection when not moving in the same direction as object movement
        collisionNormal = new Vector(0, -1); // Assume collision normal is facing upward
        objectMovementDirection = new Vector(1, 0); // Assume paddle is moving to the right
        fireball.setVelocity(2, 2); // Set initial velocity for testing
        fireball.reflect(collisionNormal, objectMovementDirection);
        expectedVelocity = new Vector(2, -2).subtract(collisionNormal.multiply(2 * fireball.getVelocity().dotProduct(collisionNormal)));
        assertEquals(expectedVelocity.getX(), fireball.getVelocity().getX(), 0.001);
        assertEquals(expectedVelocity.getY(), fireball.getVelocity().getY(), 0.001);

    }

	public void testFireballBarrierCollision() {
        
        Fireball fireball = new Fireball(533,325,16,16);
        Barrier barrier = new SimpleBarrier(533, 550); // Assume barrier is moving to the right
       barrier.setMoving();
        // Set the fireball's velocity and simulate collision with the barrier
        fireball.setVelocity(2, 2);
        Vector collisionNormal = new Vector(-1, 0); // Assume collision normal is perpendicular to barrier's movement direction
        Vector objectMovementDirection = barrier.getVelocity();
        fireball.reflect(collisionNormal, objectMovementDirection);

        // Calculate the expected velocity after reflection
        Vector expectedVelocity = new Vector(2, 2).subtract(collisionNormal.multiply(2 * fireball.getVelocity().dotProduct(collisionNormal)));

        // Check if the fireball's velocity matches the expected velocity after reflection
        assertEquals(expectedVelocity.getX(), fireball.getVelocity().getX(), 0.001);
        assertEquals(expectedVelocity.getY(), fireball.getVelocity().getY(), 0.001);
    }
}
