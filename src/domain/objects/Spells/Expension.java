package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Expension extends Spell{
    public Expension(){
        super();
        this.name="expension";
        this.isStorable=true;
        try {
            this.Img=ImageIO.read(getClass().getResource("/ui/images/extend.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
