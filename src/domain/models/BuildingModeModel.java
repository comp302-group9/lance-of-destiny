package domain.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseConnection;
import domain.objects.Barrier.Barrier;
import domain.objects.Barrier.ExplosiveBarrier;
import domain.objects.Barrier.ReinforcedBarrier;
import domain.objects.Barrier.RewardingBarrier;
import domain.objects.Barrier.SimpleBarrier;

public class BuildingModeModel {
    public static final int ROWS = 11;
    public static final int COLUMNS = 19;

    public int number_simple = 0;
    public int number_reinforced = 0;
    public int number_explosive = 0;
    public int number_rewarding = 0;
    private User user;

	private ArrayList<Barrier> BarrierList = new ArrayList<Barrier>();

	Barrier a = new SimpleBarrier();
	Barrier b = new ReinforcedBarrier(4);
	Barrier c = new ExplosiveBarrier(4);
	Barrier d = new RewardingBarrier();

	public BuildingModeModel(User user) {
		BarrierList.add(a);
		BarrierList.add(b);
		BarrierList.add(c);
		BarrierList.add(d);
		this.user = user;
	}

	public void update(long currentTime, boolean[] keys, int WIDTH, int HEIGHT) {
	}

	public ArrayList<Barrier> getBarrierList() {
		return BarrierList;
	}

	public void setBarrierList(ArrayList<Barrier> barrierList) {
		BarrierList = barrierList;
	}

	public boolean validateBarriers() {
		//Requires: Implicit dependency on internal state variables 
		//(number_simple, number_reinforced, number_explosive, number_rewarding) 
		//that store current counts of each barrier type.

		//Modifies: No external or internal state modifications.

		//Effects: Checks if barrier counts are within set limits, returning true if 
		//valid (each type meets minimum requirements and total count doesn’t exceed 219), 
		//or false otherwise.


		if (number_simple < 75 || number_reinforced < 10 || number_explosive < 5 || number_rewarding < 10) {
			return false;
		}
		else if (number_simple + number_reinforced + number_explosive + number_rewarding > 219) {
			return false;
		}
			
		return true;
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
        try (BufferedReader br = new BufferedReader(new FileReader("src\\domain\\txtData\\Test.txt"))) {
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
	
	public void writeGrid(int[][] matrix) {
	    StringBuilder gridStringBuilder = new StringBuilder();
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix[i].length; j++) {
	            gridStringBuilder.append(matrix[i][j]).append(" ");
	        }
	    }
	    String gridString = gridStringBuilder.toString().trim(); // Remove trailing space
	    
	}
	
	public void saveGridToDatabase(int[][] matrix) {
		
		//Requires: A non-null 2D integer array matrix representing the game grid.

		//Modifies: Updates the SavedGames table in the database by inserting a new row with 
		//the current game state.

		//Effects: Converts the game grid into a single string format, inserts it into the database 
		//with static game details, and handles SQL exceptions by printing error messages. 
		//This method ensures game progress persistence.
		
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO SavedGames (gameId, username, life, score, grid) VALUES (?, ?, ?, ?, ?)")) {
            // Convert the 2D array into a single string
            StringBuilder gridBuilder = new StringBuilder();
            StringBuilder gridStringBuilder = new StringBuilder();
    	    for (int i = 0; i < matrix.length; i++) {
    	        for (int j = 0; j < matrix[i].length; j++) {
    	            gridStringBuilder.append(matrix[i][j]).append(" ");
    	        }
    	    }
    	    String gridString = gridStringBuilder.toString().trim(); // Remove trailing space
            
            // Assuming gameId is auto-incremented or otherwise generated
            pstmt.setNull(1, java.sql.Types.INTEGER); // Use NULL or provide a value if not auto-increment
            pstmt.setString(2, this.user.getUsername()); // Replace with your User class's method to get user ID
            pstmt.setInt(3, 3); // Set life as 3
            pstmt.setInt(4, 0); // Set score as 0
            pstmt.setString(5, gridString); // Set grid as the constructed string

            pstmt.executeUpdate();
        } catch (SQLException e) {
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
	
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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