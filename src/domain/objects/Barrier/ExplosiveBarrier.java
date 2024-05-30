package domain.objects.Barrier;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import domain.models.RunningModeModel;

public class ExplosiveBarrier extends Barrier {

	private int explosionRadius;
	private double centerX, centerY;  // Center of circular movement
    private double angle;             // Current angle for circular movement
    private double angularSpeed;// Speed of rotation rad/s
    private static final double L = RunningModeModel.WIDTH/50; //should be /10 but resultant radius is too large
    private static final double RADIUS = 1.5 * L;
    
    private ArrayList<Debris> debrisList;
    private ArrayList<BarrierObserver> observers = new ArrayList<>();
    
    
    public ExplosiveBarrier(int explosionRadius) {
        super();
        this.explosionRadius = explosionRadius;
        this.debrisList = new ArrayList<>();
        this.centerX = this.x+this.width/2;
        this.centerY = (this.y + this.height/2) - 1.5 * L; // Center for circular movement
        this.angle = 0;
        this.message = "*At least 5*";
        updateMovementState(RunningModeModel.barriers); // Initialize movement state
    }

    public ExplosiveBarrier(int x, int y) {
        super(x, y);
        this.explosionRadius = (int) (1.5 * L);
        this.debrisList = new ArrayList<>();
        this.centerX = this.x+this.width/2;
        this.centerY = (this.y + this.height/2) - 1.5 * L; // Center for circular movement
        this.angle = 0;
        this.message = "*At least 5*";
        updateMovementState(RunningModeModel.barriers); // Initialize movement state
    }
    public void addObserver(BarrierObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BarrierObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (BarrierObserver observer : observers) {
            observer.onDebrisCreated(debrisList);
        }
    }

    // Method to update whether the barrier should move based on free space and probability
    private void updateMovementState(ArrayList<Barrier> barriers) {
        if (hasEnoughSpaceForCircularMovement(barriers)) {
            this.isMoving = new Random().nextDouble() < 1; // 20% chance of moving if there's enough space// test
            if (this.isMoving) {
                
                this.angularSpeed = 1; // Example angular speed, can be adjusted: should be 0.167  rad/s but too slow for demo purposes
            }
        } else {
            this.isMoving = false; // Don't move if there's not enough space
        }
    }
    private boolean isLeavingGameBounds() {
        return this.x - RADIUS < 0 || this.x + RADIUS > RunningModeModel.WIDTH ||
               this.y - RADIUS < 0 || this.y + RADIUS > RunningModeModel.HEIGHT;
    }

    // Method to check if there's enough space for circular movement
    private boolean hasEnoughSpaceForCircularMovement(ArrayList<Barrier> barriers) {
        double radius = 1.5 * RunningModeModel.barrierWidth;
        Circle movementCircle = new Circle(this.centerX, this.centerY, radius); // Create the circle bound

        for (Barrier barrier : barriers) {
            if (barrier != this && movementCircle.intersects(barrier.getBounds())) {
                return false; // Not enough space if another barrier intersects the circular path
            }
        }
        System.out.println("Enough space!");
        return true;
    }

    // Updated move method for circular movement
    public void move(ArrayList<Barrier> barriers, double deltaTime) {
        if (isMoving) {
            // Save old coordinates for logging
            

            // Update position based on circular movement
            double newAngle = this.angle + this.angularSpeed * deltaTime;
            assert newAngle != 0 : "The new angle of rotation should not be zero"; // Ensure the new angle is not zero

            int newX = (int) (this.centerX + this.explosionRadius * Math.cos(newAngle));
            int newY = (int) (this.centerY + this.explosionRadius * Math.sin(newAngle));

            // Check for collision with other barriers and reverse direction if needed
            if (isCollidingWithOtherBarriers(newX, newY, barriers)||isLeavingGameBounds()) {
                reverseDirection();
                newAngle = this.angle + this.angularSpeed * deltaTime;
                newX = (int) (this.centerX + this.explosionRadius * Math.cos(newAngle));
                newY = (int) (this.centerY + this.explosionRadius * Math.sin(newAngle));
            } else {
                this.angle = newAngle;
            }

            this.x = newX;
            this.y = newY;

            

            
            
        }
    }

    // Check for collision with other barriers at new coordinates
    private boolean isCollidingWithOtherBarriers(int newX, int newY, ArrayList<Barrier> barriers) {
        for (Barrier barrier : barriers) {
            if (barrier != this && barrier.getBounds().intersects(new Rectangle(newX, newY, this.width, this.height))) {
                return true;
            }
        }
        return false;
    }

    public void reverseDirection() {
        this.angularSpeed = -this.angularSpeed;
    }

	private void createFallingDebris() {
        // Create debris pieces and add to the list
        for (int i = 0; i < 3; i++) { // Example: create 5 pieces of debris
            int debrisX = this.x + new Random().nextInt(this.width*2);
            int debrisY = this.y + new Random().nextInt(this.height*2);
            debrisList.add(new Debris(debrisX, debrisY));
        }
        notifyObservers();
    }
	@Override
	public boolean onHit() {
		createFallingDebris();
		return true;
	}

	@Override
	public String getName() {
		return "Explosive Barrier";
	}

	@Override
	public String getImg() {
		return "/ui/images/explosiveBarrier.png";
	}

	public int getExplosionRadius() {
		return explosionRadius;
	}
	public ArrayList<Debris> getDebrisList() {
        return debrisList;
    }

public class Circle {
    private double x, y, radius;

    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public boolean intersects(Rectangle rect) {
        double nearestX = Math.max(rect.getX(), Math.min(x, rect.getX() + rect.getWidth()));
        double nearestY = Math.max(rect.getY(), Math.min(y, rect.getY() + rect.getHeight()));
        double deltaX = x - nearestX;
        double deltaY = y - nearestY;
        return (deltaX * deltaX + deltaY * deltaY) < (radius * radius);
    }
}

}