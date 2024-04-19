package domain.objects.Barrier;

public class SimpleBarrier extends Barrier {

	public SimpleBarrier() {
		super();
	}

	public SimpleBarrier(int x, int y){
		super(x, y);
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
