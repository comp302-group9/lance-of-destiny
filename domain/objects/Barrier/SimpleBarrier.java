package domain.objects.Barrier;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import domain.models.RunningModeModel;

public class SimpleBarrier extends Barrier {

	public SimpleBarrier() {
		super();
		this.message="*At least 75*";
		
	}

	public SimpleBarrier(int x, int y){
		super(x, y);
		this.isMoving = new Random().nextDouble() < 0.2; // 20% chance of moving
		
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
            }
        }
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