package domain.objects.Barrier;

public class ReinforcedBarrier extends Barrier {

	private int hitCount;

	public ReinforcedBarrier(int hitCount) {
		this.hitCount = hitCount;
	}

	@Override
	public void onHit() {
		hitCount--;
		if (hitCount <= 0) {
			System.out.println("Reinforced Barrier destroyed");
		}
	}

	@Override
	public String getName() {
		return "Reinforced Barrier";
	}
}
