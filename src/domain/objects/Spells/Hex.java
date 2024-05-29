package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

import domain.DEFAULT;
import domain.objects.Paddle;

public class Hex extends Spell{
	private Paddle paddle;
	
    public Hex(Paddle paddle){
        super();
        this.paddle = paddle;
        this.name="expension";
        try {
            this.Img=ImageIO.read(getClass().getResource("/ui/images/hex.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    public void Activate(){
    	if (paddle != null) {
            paddle.setHexActive(true);
            setActive(true);
            startTimer();
        }
    }

    @Override
    public void deActivate() {
    	if (paddle != null) {
            paddle.setHexActive(false);
            setActive(false);
        }
    }
}
