package domain.objects.Spells;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import domain.models.RunningModeModel;
import domain.objects.Barrier.Barrier;
import network.Connectable;

public class InfiniteVoid extends Spell {
	Random random = new Random();
	Connectable con;

	public InfiniteVoid() {
		super();
		this.name = "InfiniteVoid";
		this.color = new Color(128, 0, 128, 250);
		this.duration=15;
		try {
			this.Img = ImageIO.read(getClass().getResource("/ui/images/InfiniteVoid.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Activate() {
		int l=8;
		int size=RunningModeModel.barriers.size();
		if(size<8)l=size;
		
		for(int i=0;i<l;i++) {
			Barrier bar=RunningModeModel.barriers.get(random.nextInt(size));
			if (!bar.getFrozen())bar.freeze();
			else i--;
		}
		System.out.println("infiniteVoid!!");
		
	}

	@Override
	public void deActivate() {

	}
}