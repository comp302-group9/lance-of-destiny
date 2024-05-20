package domain.objects.Barrier;

public class ExplosiveBarrier extends Barrier {

	private int explosionRadius;

	public ExplosiveBarrier(int explosionRadius) {
		super();
		this.explosionRadius = explosionRadius;
		this.message="*At least 5*";
	}

	public ExplosiveBarrier(int x, int y, int gridX, int gridY) {
		super(x,y, gridX, gridY);
	}

	@Override
	public boolean onHit() {
		System.out.println("Explosive barrier destroyed");
		return true;
	}

	@Override
	public String getName() {
		return "Explosive Barrier";
	}

	@Override
	public String getImg() {
		return "/ui/images/explosiveBarrier.png";
	}

	public int getExplosionRadius() {
		return explosionRadius;
	}

}