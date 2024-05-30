package domain.objects.Barrier;

import domain.controllers.CollisionHandler;
import domain.objects.Fireball;

public class HollowPurpleBarrier extends SimpleBarrier{

	public HollowPurpleBarrier(int x, int y) {
		super(x,y);// TODO Auto-generated constructor stub
		this.img="/src/ui/images/purplebarrier.png";
		
	}
	public boolean onHit() {
		return false;
	}
public void update() {
	
}
public boolean checkCollisionWithFireball(Fireball f) {
	return CollisionHandler.CollisionCheck(this, f);
}
}
