package domain.objects.Barrier;

import java.util.ArrayList;
import java.util.Random;

import domain.models.RunningModeModel;
import domain.objects.Box;

public class RewardingBarrier extends Barrier {
	private String containedSpell;
	static String[] spells= {"A", "B", "C"};

	public RewardingBarrier() {
		super();
		this.containedSpell=spells[new Random().nextInt(spells.length)];
		this.message="*At least 10*";
	}

	public RewardingBarrier(int x, int y) {
		super(x,y);
		this.containedSpell=spells[new Random().nextInt(spells.length)];
	}

	@Override
	public boolean onHit() {
		RunningModeModel.boxes.add(new Box(x + (RunningModeModel.barrierWidth/2),y + (RunningModeModel.barrierWidth/2)));
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

	@Override
	public void move(ArrayList<Barrier> barriers, double deltaTime) {}
	
	public void addObserver(BarrierObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BarrierObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {

    }

}