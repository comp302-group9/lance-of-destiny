package domain.objects.Spells;

import java.awt.Color;
import java.awt.print.Paper;
import java.io.IOException;

import javax.imageio.ImageIO;


import domain.objects.Fireball;

public class DoubleAccel extends Spell{
    
	

    private Fireball fireball;
    private double originalDx, originalDy;

    public DoubleAccel(Fireball fireball) {
        super();
        this.fireball = fireball;
        this.name = "DoubleAccel";
        this.duration = 15; // Set duration to 15 seconds
        try {
            this.Img = ImageIO.read(getClass().getResource("/ui/images/doubleAccel.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Activate() {
        originalDx = fireball.getVelocityX();
        originalDy = fireball.getVelocityY();
        fireball.setVelocity(originalDx / 2, originalDy / 2);
       
        
        setActive(true);
        
    }

    @Override
    public void deActivate() {
        fireball.setVelocity(originalDx, originalDy);
       
        
        setActive(false);
        
    }
}
