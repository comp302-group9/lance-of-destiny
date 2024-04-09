package domain.objects.Barrier;


public class RewardingBarrier extends Barrier {
	private String containedSpell;

	public RewardingBarrier(int x, int y, String containedSpell) {
		super(x, y);
		this.containedSpell = containedSpell;
	}

	@Override
	public void onHit() {
		System.out.println("Rewarding Barrier destroyed");
	}

	// Getter for containedSpell
	public String getContainedSpell() {
		return containedSpell;
	}
}
