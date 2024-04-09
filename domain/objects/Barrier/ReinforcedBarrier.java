package domain.objects.Barrier;


public class ReinforcedBarrier extends Barrier {

	private int hitCount;

	public ReinforcedBarrier(int x, int y, int hitCount) {
		super(x, y);
		this.hitCount = hitCount;
	}

	@Override
	public void onHit() {
		hitCount--;
		if (hitCount <= 0) {
			System.out.println("Reinforced Barrier destroyed");
		}
	}
}
