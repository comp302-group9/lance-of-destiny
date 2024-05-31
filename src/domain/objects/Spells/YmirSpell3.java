package domain.objects.Spells;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import domain.DEFAULT;
import domain.models.RunningModeModel;
import domain.objects.Barrier.Barrier;
import domain.objects.Barrier.HollowPurpleBarrier;

public class YmirSpell3 extends Spell{
    //Hollow purple
	private ArrayList<HollowPurpleBarrier> hollowPurpleBarriers=new ArrayList<>();
	private RunningModeModel model;
	public YmirSpell3(RunningModeModel m){
        super();
        this.duration=0;
        this.model= m;
        this.name = "Hollow Purple";
        //this.color=new Color(128, 0, 128, 250);
        try {
            this.Img=ImageIO.read(getClass().getResource("/ui/images/fireballSpell.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void Activate(){
        System.out.println(model.getUser().getUsername());
    	addHollowPurpleBarriers();
    	setActive(true);
        startTimer();
    }

    @Override
    public void deActivate() {
    	//removeHollowPurpleBarriers();
    	setActive(false);

    }
    public ArrayList<int[]> getValidGridPositions(int[][] grid) {
        ArrayList<int[]> validPositions = new ArrayList<>();

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == 0) {
                    validPositions.add(new int[]{x, y});
                }
            }
        }

        return validPositions;
    }

    public HashMap<Integer, ArrayList<Barrier> > movingBarriersInRow(ArrayList<int[]> validPositions){
    	HashMap<Integer, ArrayList<Barrier>> map = new HashMap<Integer, ArrayList<Barrier>>();
    	for (int[] position: validPositions) {
    	 ArrayList<Barrier> moving = new ArrayList<Barrier>();
    	 for (Barrier b: model.barriers) {
    		 if (b.getGridX()== position[0] && b.isMoving ) {
    			 moving.add(b);
    		 }
    	 }
    	 map.put(position[0], moving);

    	}
    	return map;

    }
    private void addHollowPurpleBarriers() {
        Random random = new Random();
        int mapHeight = RunningModeModel.HEIGHT;
        int mapWidth = RunningModeModel.WIDTH;
        int barriersAdded = 0;
        ArrayList<int[]>validPositions = getValidGridPositions(model.getGrid());
        HashMap<Integer, ArrayList<Barrier>> map = movingBarriersInRow(validPositions);
        
        if (validPositions.isEmpty()){
        	return;
        }
        	
        
        
        //HashMap<Integer, ArrayList<Barrier>> map = movingBarriersInRow(validPositions);

        while (barriersAdded < 8 || barriersAdded == validPositions.size()) {

            int [] position = validPositions.get(random.nextInt(validPositions.size()));
            
            int row = position[0];
            int col = position[1];
            int xStart = DEFAULT.screenHeight / 32;
            int yStart = DEFAULT.screenWidth / 32;
            int xGap = DEFAULT.screenHeight / 128;
            int yGap = DEFAULT.screenHeight / 96;
            int x = xStart + col * (model.barrierWidth + xGap);
            int y = yStart + row * (model.barrierHeight + yGap);

            if (isValidPosition(row, col, x,y, map)) {
                HollowPurpleBarrier barrier = new HollowPurpleBarrier(x, y);
                //model.addPurpleBarrier(barrier);
                barrier.setGridX(row);
        		barrier.setGridY(col);
                hollowPurpleBarriers.add(barrier);
                model.addPurpleBarrier(barrier);

                barriersAdded++;
            }
        }
        return;
    }
    private boolean isValidPosition(int gridX, int gridY, int x, int y, HashMap<Integer,ArrayList<Barrier>> map) {
    	Rectangle possiblePlacement = new Rectangle(x,y+model.barrierWidth, model.barrierWidth, model.barrierHeight);

    	//ArrayList<Barrier> movingToCheck = map.get(x);
    	ArrayList<Barrier> movingToCheck = map.get(gridX);
    	if (movingToCheck.isEmpty()) {
    		return true;
    	}
    	
    	else {
    	
    		for (Barrier b: movingToCheck) {
    			if (b.getFutureBounds().intersects(possiblePlacement)|| b.getBounds().intersects(possiblePlacement)) {
    				return false;
    			}
    		}
    	}

        return true;
    }

    private void removeHollowPurpleBarriers() {
        for (HollowPurpleBarrier barrier : hollowPurpleBarriers) {
            model.removePurpleBarrier(barrier);
        }
        hollowPurpleBarriers.clear();
    }

}