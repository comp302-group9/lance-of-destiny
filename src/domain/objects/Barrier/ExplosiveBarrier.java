package domain.objects.Barrier;

import java.awt.Graphics;
import java.util.ArrayList;

public class ExplosiveBarrier extends Barrier {

	private int explosionRadius;
	private boolean fall=true;

	public ExplosiveBarrier(int explosionRadius) {
		super();
		this.explosionRadius = explosionRadius;
		this.message="*At least 5*";
	}

	public ExplosiveBarrier(int x, int y) {
		super(x,y);
	}

	@Override
	public boolean onHit() {
		fall=true;
		return true;
	}

	public void move(ArrayList<Barrier> barriers, double deltaTime){
		if(fall){
			setY(getY()+5);
		}	
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