package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Overwhelm extends Spell{
    public Overwhelm(){
        super();
        this.name="overwhelm";
        this.isStorable=true;
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
