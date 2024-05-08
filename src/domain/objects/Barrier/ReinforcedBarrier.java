package domain.objects.Barrier;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import domain.models.RunningModeModel;

public class ReinforcedBarrier extends Barrier {

	private int hitCount =3;

	public ReinforcedBarrier(int hitCount) {
		this.hitCount = hitCount;
		this.message="*At least 10*";
		
		
		
	}

	public ReinforcedBarrier(int x,int y) {
		super(x,y);
		//this.hitCount=new Random().nextInt(3)+2;
		updateMovementState(RunningModeModel.barriers); // Update whether it should move
		
	}
	
	private void updateMovementState(ArrayList<Barrier> barriers) {
        if (!hasBarrierOnImmediateLeft(barriers) && !hasBarrierOnImmediateRight(barriers)) {
            this.isMoving = new Random().nextDouble() < 0.2; // 20% chance of moving if there's free space
        } else {
            this.isMoving = false; // Don't move if barriers are on the immediate left or right
        }
    }
    
	
	
	public void move(ArrayList<Barrier> barriers, double deltaTime) {
		if (isMoving) {
			if (hasBarrierOnImmediateLeft(barriers) && hasBarrierOnImmediateRight(barriers)) {
                return; // Don't move if barriers on both sides
            }
        
            double movement = (33 * 900 / 512) / 4 * deltaTime; // L/4 per second
            
            

            x+=direction;

            // Check for collisions with other barriers
            if (isCollidingWithOtherBarriers(barriers) || x < 0 || x + width > RunningModeModel.WIDTH) {
                reverseDirection(); // Reverse if collision or hitting boundaries
                x = Math.min(Math.max(x, 0), RunningModeModel.WIDTH - width); // Clamp within boundaries
            }
         
        }
    }
	
	
	

	@Override
	public boolean onHit() {
		hitCount--;
		if (hitCount <= 0) {
			return true;
		}
		return false;
	}

	public void draw(Graphics g) {
		// Draw the fireball
		super.draw(g);
		
		// Draw a white circle on top of the image
		int circleRadius = 8; // adjust the radius as needed
		g.setColor(Color.WHITE);
		g.fillOval(x + RunningModeModel.barrierWidth / 2 - circleRadius, y + RunningModeModel.barrierHeight / 2 - circleRadius, 2 * circleRadius, 2 * circleRadius);
		
		// Draw the hit count text on the circle
		g.setColor(Color.BLACK); // Set color to black for the text
		Font font = new Font("Arial", Font.CENTER_BASELINE, 12); // Define font for the text
		g.setFont(font);
		String hitCountText = String.valueOf(hitCount); // Convert hitCount to a string
		FontMetrics metrics = g.getFontMetrics(font);
		int textWidth = metrics.stringWidth(hitCountText);
		int textHeight = metrics.getHeight();
		g.drawString(hitCountText, x + RunningModeModel.barrierWidth / 2 - textWidth / 2, y + RunningModeModel.barrierHeight / 2 + textHeight / 4); // Draw the text centered on the circle
	}
	

	@Override
	public String getName() {
		return "Reinforced Barrier";
	}
	
	@Override
	public String getImg() {
		return "/ui/images/reinforcedBarrier.png";
	}
}