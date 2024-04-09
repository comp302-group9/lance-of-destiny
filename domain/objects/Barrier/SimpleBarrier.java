package domain.objects.Barrier;


public class SimpleBarrier extends Barrier {

	public SimpleBarrier(int x, int y) {
		super(x, y);
	}

	@Override
	public void onHit() {
		System.out.println("Reinforced Barrier destroyed");
	}
}

