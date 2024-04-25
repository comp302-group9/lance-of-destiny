package domain.objects.Barrier;

public class SimpleBarrier extends Barrier {

	public SimpleBarrier() {
		super();
		this.message="*At least 75*";
	}

	public SimpleBarrier(int x, int y){
		super(x, y);
	}
	 public void move() {
	        if (isMoving) {
	            // Move the barrier horizontally based on its direction
	            if (direction == 0) {
	                x -= 90 / 4; // Move left
	            } else {
	                x += 90 / 4; // Move right
	            }
	            // Reverse direction if about to collide with another barrier
	            // Implement collision detection logic here
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