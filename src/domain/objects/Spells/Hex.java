package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

import domain.DEFAULT;

public class Hex extends Spell{
    public Hex(){
        super();
        this.name="expension";
        this.isStorable=true;
        try {
            this.Img=ImageIO.read(getClass().getResource("/ui/images/hex.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    public void Activate(){
    }

    @Override
    protected void deActivate() {
    }
}
