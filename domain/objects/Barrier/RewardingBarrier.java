package domain.objects.Barrier;

import java.util.Random;

public class RewardingBarrier extends Barrier {
	private String containedSpell;
	static String[] spells= {"A", "B", "C"};

	public RewardingBarrier() {
		super();
		this.containedSpell=spells[new Random().nextInt(spells.length)];
	}

	public RewardingBarrier(int x, int y) {
		super(x,y);
		this.containedSpell=spells[new Random().nextInt(spells.length)];
	}

	@Override
	public boolean onHit() {
		System.out.println("Rewarding Barrier destroyed");
		return true;
	}

	@Override
	public String getName() {
		return "Rewarding Barrier";
	}

	// Getter for containedSpell
	public String getContainedSpell() {
		return containedSpell;
	}
	
	@Override
	public String getImg() {
		return "/ui/images/rewardingBarrier.png";
	}

}
