package domain.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import domain.objects.Barrier.Barrier;
import domain.objects.Barrier.ExplosiveBarrier;
import domain.objects.Barrier.ReinforcedBarrier;
import domain.objects.Barrier.RewardingBarrier;
import domain.objects.Barrier.SimpleBarrier;

public class BuildingModeModel {
    public static final int ROWS = 10;
    public static final int COLUMNS = 14;

    public int number_simple = 0;
    public int number_reinforced = 0;
    public int number_explosive = 0; 
    public int number_rewarding = 0; 

	private ArrayList<Barrier> BarrierList = new ArrayList<Barrier>();

	Barrier a = new SimpleBarrier(); 
	Barrier b = new ReinforcedBarrier(4);
	Barrier c = new ExplosiveBarrier(4);
	Barrier d = new RewardingBarrier();

	public BuildingModeModel() {
		BarrierList.add(a);
		BarrierList.add(b);
		BarrierList.add(c);
		BarrierList.add(d);
	}

	public void update(long currentTime, boolean[] keys, int WIDTH, int HEIGHT) {
	}

	public ArrayList<Barrier> getBarrierList() {
		return BarrierList;
	}

	public void setBarrierList(ArrayList<Barrier> barrierList) {
		BarrierList = barrierList;
	}

	public void validate() {

	}

    public int[][] createEmptyGrid(){
        int[][] grid = new int[ROWS][COLUMNS];
        for (int i=0; i<ROWS ;i++){
            for (int j=0; j<COLUMNS ;j++){
                grid[i][j]=0;
            }
        }
        return grid;
    }

    public int[][] readTxt(String fileName){
        int[][] matrix = new int[ROWS][COLUMNS];
        try (BufferedReader br = new BufferedReader(new FileReader("domain\\txtData\\Test.txt"))) {
            String line;
            int row = 0;
            
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(" ");
                for (int col = 0; col < elements.length; col++) {
                    matrix[row][col] = Integer.parseInt(elements[col]);
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

	public void writeTxt(String fileName, int[][] matrix) {
		try (FileWriter writer = new FileWriter(fileName)) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					writer.write(matrix[i][j] + " ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public int getNumber_simple() {
		return number_simple;
	}

	public void setNumber_simple(int number_simple) {
		this.number_simple = number_simple;
	}

	public int getNumber_reinforced() {
		return number_reinforced;
	}

	public void setNumber_reinforced(int number_reinforced) {
		this.number_reinforced = number_reinforced;
	}

	public int getNumber_explosive() {
		return number_explosive;
	}

	public void setNumber_explosive(int number_explosive) {
		this.number_explosive = number_explosive;
	}

	public int getNumber_rewarding() {
		return number_rewarding;
	}

	public void setNumber_rewarding(int number_rewarding) {
		this.number_rewarding = number_rewarding;
	}

}