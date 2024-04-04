
public class ExplosiveBarrier extends Barrier {

	private int explosionRadius;

	public ExplosiveBarrier(int x, int y, int explosionRadius) {
		super(x, y);
		this.explosionRadius = explosionRadius;
	}

	@Override
	public void onHit() {
		System.out.println("Explosive barrier destroyed");
	}

	public int getExplosionRadius() {
		return explosionRadius;
	}
}
