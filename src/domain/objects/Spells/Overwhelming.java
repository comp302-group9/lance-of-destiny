package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Overwhelming extends Spell  {
	
    public Overwhelming(boolean initialState) {
        this.name = "fireballSpell";
        this.isStorable = false;
        
        try {
            this.Img = ImageIO.read(getClass().getResource("/ui/images/fireballSpell.png")); // Set the image path
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void Activate() {
        if (!active) {
            active = true;
            startTimer();
            // Implement additional logic to make the fireball more powerful
            // and allow it to pass through barriers
            System.out.println(name + " activated. It will last for " + duration + " seconds.");
        }
    }

    @Override
    protected void deActivate() {
        // Implement logic to deactivate the spell and reset its effects
        System.out.println(name + " deactivated.");
    }

}
