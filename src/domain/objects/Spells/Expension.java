package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

import domain.DEFAULT;

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
 
    public void Activate(){
        DEFAULT.paddleWidth=DEFAULT.screenWidth/5;
        setActive(true);
    }

    @Override
    public void deActivate() {
        DEFAULT.paddleWidth=DEFAULT.screenWidth/10;
        setActive(false);
    }
}
