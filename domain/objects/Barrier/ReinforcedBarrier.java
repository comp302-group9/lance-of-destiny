package domain.objects.Barrier;

public class ReinforcedBarrier extends Barrier {

	private int hitCount =3;

	public ReinforcedBarrier(int hitCount) {
		this.hitCount = hitCount;
	}

	public ReinforcedBarrier(int x,int y) {
		super(x,y);
	}

	@Override
	public boolean onHit() {
		hitCount--;
		if (hitCount <= 0) {
			System.out.println("Reinforced Barrier destroyed");
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "Reinforced Barrier";
	}
	
	@Override
	public String getImg() {
		return "/ui/images/reinforcedBarrier.png";
	}
}
