package domain.objects.Barrier;

public class ExplosiveBarrier extends Barrier {

	private int explosionRadius;

	public ExplosiveBarrier(int explosionRadius) {
		this.explosionRadius = explosionRadius;
	}

	@Override
	public void onHit() {
		System.out.println("Explosive barrier destroyed");
	}

	@Override
	public String getName() {
		return "Explosive Barrier";
	}

	@Override
	public String getImg() {
		return "images/explosiveBarrier.png";
	}

	public int getExplosionRadius() {
		return explosionRadius;
	}

}
