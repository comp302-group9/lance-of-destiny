package domain.objects.Barrier;

import java.util.ArrayList;
import java.util.Random;

import domain.models.RunningModeModel;

public class SimpleBarrier extends Barrier {

	public SimpleBarrier() {
		super();
		this.message="*At least 75*";
		
	}

	public SimpleBarrier(int x, int y, int gridX, int gridY){
		super(x, y, gridX, gridY);
		updateMovementState(RunningModeModel.barriers); // Initialize movement state
		
	}
	
	// Method to update whether the barrier should move based on free space and probability
    private void updateMovementState(ArrayList<Barrier> barriers) {
        if (!hasBarrierOnImmediateLeft(barriers) && !hasBarrierOnImmediateRight(barriers)) {
            this.isMoving = new Random().nextDouble() < 0.2; // 20% chance of moving if there's free space
        } else {
            this.isMoving = false; // Don't move if barriers are on the immediate left or right
        }
    }
	
	
	
	public void move(ArrayList<Barrier> barriers, double deltaTime) {
		if (isMoving) {
			if (hasBarrierOnImmediateLeft(barriers) && hasBarrierOnImmediateRight(barriers)) {
				return; // Don't move if barriers on both sides
            }
            double movement = (33 * 900 / 512) / 4 * deltaTime; // L/4 per second

            if (direction == 0) {
                x -= 1; // Move left
            } else {
                x += 1; // Move right
            }

            // Check for collisions with other barriers
            if (isCollidingWithOtherBarriers(barriers) || x < 0 || x + width > RunningModeModel.WIDTH) {
                reverseDirection(); // Reverse if collision or hitting boundaries
                x = Math.min(Math.max(x, 0), RunningModeModel.WIDTH - width); // Clamp within boundaries
            }
        }
		updateMovementState(barriers);
    }
	 
	 
	@Override
	public boolean onHit() {
		System.out.println("Reinforced Barrier destroyed");
		return true;
	}

	@Override
	public String getName() {
		return "Simple Barrier";
	}
	
	@Override
	public String getImg() {
		return "/ui/images/simpleBarrier.png";
	}
}