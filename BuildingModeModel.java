import java.util.ArrayList;

import domain.Barrier;
import domain.ExplosiveBarrier;
import domain.ReinforcedBarrier;
import domain.RewardingBarrier;
import domain.SimpleBarrier;

public class BuildingModeModel {

	private ArrayList<Barrier> BarrierList = new ArrayList<Barrier>();

	Barrier a = new SimpleBarrier();
	Barrier b = new ReinforcedBarrier(4);
	Barrier c = new ExplosiveBarrier(4);
	Barrier d = new RewardingBarrier("x");

	public BuildingModeModel() {
		BarrierList.add(a);
		BarrierList.add(b);
		BarrierList.add(c);
		BarrierList.add(d);
	}

	public ArrayList<Barrier> getBarrierList() {
		return BarrierList;
	}

	public void setBarrierList(ArrayList<Barrier> barrierList) {
		BarrierList = barrierList;
	}

	public void validate() {

	}
}
