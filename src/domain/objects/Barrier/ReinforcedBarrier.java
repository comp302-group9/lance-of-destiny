package domain.objects.Barrier;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

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
	}

	@Override
	public boolean onHit() {
		hitCount--;
		if (hitCount <= 0) {
			System.out.println("Reinforced Barrier destroyed");
			return true;
		}
		return false;
	}

	public void draw(Graphics g) {
		// Draw the fireball
		if (image != null) {
			g.drawImage(image, x, y, RunningModeModel.barrierWidth, RunningModeModel.barrierHeight, null);
		}
		
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
