package domain.objects.Barrier;

public class SimpleBarrier extends Barrier {

	public SimpleBarrier() {
	}

	@Override
	public void onHit() {
		System.out.println("Reinforced Barrier destroyed");
	}

	@Override
	public String getName() {
		return "Simple Barrier";
	}
}
