package domain.objects.Spells;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;

public class YmirSpell3 extends Spell{
    
	
	public YmirSpell3(){
        super();
        this.name = "overwhelm";
        this.color=new Color(128, 0, 128, 250);
        try {
            this.Img=ImageIO.read(getClass().getResource("/ui/images/fireballSpell.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void Activate(){

    }

    @Override
    public void deActivate() {
        
    }
}
