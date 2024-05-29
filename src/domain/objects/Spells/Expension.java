package domain.objects.Spells;

import java.io.IOException;

import javax.imageio.ImageIO;

import domain.DEFAULT;
import domain.objects.Paddle;

public class Expension extends Spell{
	
	private Paddle paddle;
	
    public Expension(Paddle paddle){
        super();
        this.paddle = paddle;
        this.name="expension";
        try {
            this.Img=ImageIO.read(getClass().getResource("/ui/images/extend.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    public void Activate(){
    	paddle.setWidth(DEFAULT.screenWidth / 5);
        setActive(true);
    }

    @Override
    public void deActivate() {
    	 paddle.setWidth(DEFAULT.screenWidth / 10);
         setActive(false);
    }
}

