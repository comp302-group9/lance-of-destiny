package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

import domain.models.RunningModeModel;

public class Felicis extends Spell {
	
	private RunningModeModel model;

    public Felicis(RunningModeModel model) {
        super();
        this.duration = 0;
        this.model = model;
        this.name = "felicis";
        this.isStorable = true;
        try {
            this.Img = ImageIO.read(getClass().getResource("/ui/images/Heart.png")); // Update with the correct image path
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Activate() {
        model.increaseChance();
        setActive(true);
    }

    @Override
    public void deActivate() {
        // Since Felicis effect is permanent, deActivate can be empty or log a message
        setActive(false);
    }


}
