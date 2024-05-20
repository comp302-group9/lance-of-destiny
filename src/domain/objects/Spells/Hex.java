package domain.objects.Spells;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import domain.DEFAULT;


public class Hex extends Spell {
	
    private boolean canFire;
    private BufferedImage canonImg;

    public Hex() {
        super();
        this.name = "hex";
        this.isStorable = true;
        this.canFire = false;
        
        try {
            this.Img = ImageIO.read(getClass().getResource("/ui/images/Hex.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Activate() {
        if (!this.active) {
            this.active = true;
            this.canFire = true;
            startTimer();
            equipCanons();
            // Add additional logic to handle hex hits and rotation of the staff
        }
    }

    @Override
    protected void deActivate() {
        this.active = false;
        this.canFire = false;
        removeCanons();
        // Add additional logic to handle deactivation of the hex spell
    }

    private void equipCanons() {
        // Logic to equip the Magical Staff with two canons
        // The canons should point upwards and rotate as the Magical Staff rotates
        System.out.println("Canons equipped to the Magical Staff.");
    }

    private void removeCanons() {
        // Logic to remove the canons from the Magical Staff
        System.out.println("Canons removed from the Magical Staff.");
    }

    public void fireHex() {
        if (this.canFire) {
            // Logic to fire a hex that can hit the barriers
            // A hex hit should have the same effect as the hit of a Fire Ball
            System.out.println("Hex fired!");
        }
    }

    public void handleKeyPress(char key) {
        if (key == 'H' || key == 'h') {
            Activate();
        }
    }

    // Additional methods to handle the rotation and firing logic can be added here
	
	

}
