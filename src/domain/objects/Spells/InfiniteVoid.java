package domain.objects.Spells;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import domain.models.RunningModeModel;
import domain.objects.Barrier.Barrier;

public class InfiniteVoid extends Spell {
	ArrayList<Barrier> barriers;
	Random random = new Random();

	public InfiniteVoid(ArrayList<Barrier> barriers) {
		super();
		this.barriers = RunningModeModel.barriers;
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
		if(RunningModeModel.barriers.size()<8)l=RunningModeModel.barriers.size();
		
		for(int i=0;i<l;i++) {
			Barrier bar=RunningModeModel.barriers.get(random.nextInt(barriers.size()));
			if (!bar.getFrozen())bar.freeze();
			else i--;
		}
		
	}

	@Override
	public void deActivate() {

	}
}