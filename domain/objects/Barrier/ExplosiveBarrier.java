package domain.objects.Barrier;

import java.awt.Graphics;

public class ExplosiveBarrier extends Barrier {

	private int explosionRadius;

	public ExplosiveBarrier(int explosionRadius) {
		super();
		this.explosionRadius = explosionRadius;
	}

	public ExplosiveBarrier(int x, int y) {
		super(x,y);
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
