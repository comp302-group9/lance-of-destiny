package PhysicsEngines;

public interface PhysicsObject {
	Vector getPosition();
	boolean isMoving();
	Vector getVelocity();
	int getWidth();
	int getHeight();
}
