package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

import domain.objects.Fireball;

public class Overwhelm extends Spell{
	private Fireball fireball;
    
	
	public Overwhelm(Fireball fireball){
        super();
        this.fireball= fireball;
        this.name = "overwhelm";
        this.isStorable=true;
        try {
            this.Img=ImageIO.read(getClass().getResource("/ui/images/fireballSpell.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void Activate(){
    	
    	if (fireball != null) {
            fireball.setOverwhelmed(true);
            setActive(true);
            startTimer();
        }
    }

    @Override
    public void deActivate() {
    	if (fireball != null) {
            fireball.setOverwhelmed(false);
            setActive(false);
        }
    }
}
