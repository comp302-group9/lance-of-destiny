package domain.objects.Barrier;

import java.util.ArrayList;
import java.util.Random;

import domain.models.RunningModeModel;

public class ExplosiveBarrier extends Barrier {

	private int explosionRadius;
	private double centerX, centerY;  // Center of circular movement
    private double angle;             // Current angle for circular movement
    private double angularSpeed;      // Speed of rotation
    private ArrayList<Debris> debrisList;
    public ExplosiveBarrier(int explosionRadius) {
        super();
        this.explosionRadius = explosionRadius;
        this.debrisList = new ArrayList<>();
        this.message = "*At least 5*";
        this.isMoving = checkIfMoving();
        if (isMoving) {
            this.centerX = this.x;
            this.centerY = this.y - 1.5 * RunningModeModel.WIDTH; // Assuming L = width of barrier
            this.angle = 0;
            this.angularSpeed = Math.PI / 180; // Example angular speed, can be adjusted
        }
    }
    public ExplosiveBarrier(int x, int y, int gridX, int gridY) {
        super(x, y, gridX, gridY);
        this.explosionRadius = (int) (1.5 * RunningModeModel.barrierWidth);
        this.debrisList = new ArrayList<>();
        this.message = "*At least 5*";
    }

	
	public void move(ArrayList<Barrier> barriers, double deltaTime) {
        if (isMoving) {
            // Update position based on circular movement
            this.angle += this.angularSpeed * deltaTime;
            this.x = (int) (this.centerX + this.explosionRadius * Math.cos(this.angle));
            this.y = (int) (this.centerY + this.explosionRadius * Math.sin(this.angle));
            
            // Check for collision with other barriers and reverse direction if needed
            if (isCollidingWithOtherBarriers(barriers)) {
                reverseDirection();
                this.angle += this.angularSpeed * deltaTime;
                this.x = (int) (this.centerX + this.explosionRadius * Math.cos(this.angle));
                this.y = (int) (this.centerY + this.explosionRadius * Math.sin(this.angle));
            }
            for (Debris debris : debrisList) {
                debris.update();
            }
        }
    }
	private void createFallingDebris() {
        // Create debris pieces and add to the list
        for (int i = 0; i < 5; i++) { // Example: create 5 pieces of debris
            int debrisX = this.x + new Random().nextInt(this.width);
            int debrisY = this.y + new Random().nextInt(this.height);
            debrisList.add(new Debris(debrisX, debrisY));
        }
    }
	@Override
	public boolean onHit() {
		System.out.println("Explosive barrier destroyed");
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

}