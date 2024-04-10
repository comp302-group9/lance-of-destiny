package domain;

public class RewardingBarrier extends Barrier {
	private String containedSpell;

	public RewardingBarrier(String containedSpell) {
		this.containedSpell = containedSpell;
	}

	@Override
	public void onHit() {
		System.out.println("Rewarding Barrier destroyed");
	}

	@Override
	public String getName() {
		return "Rewarding Barrier";
	}

	// Getter for containedSpell
	public String getContainedSpell() {
		return containedSpell;
	}

}
