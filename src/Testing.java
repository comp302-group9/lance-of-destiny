
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import database.DatabaseConnection;
import domain.models.BuildingModeModel;
import domain.models.GameSession;
import domain.models.User;

public class Testing {
	
	private User user = new User("test1", "123", "123");
	private GameSession session;
	BuildingModeModel model = new BuildingModeModel(user);
    
	@Test
	//BB testing
    public void testGameSessionInitialization() {
        session = new GameSession(user, 1, 3, 100, "0 1 2 3");

        assertEquals(1, session.getGameId());
        assertEquals(3, session.getLife());
        assertEquals(100, session.getScore());
        assertEquals("0 1 2 3", session.getGrid());
    }
	
	
	@Test
	//BB testing
	public void testCreateEmptyGrid() {
	    int[][] grid = model.createEmptyGrid();
	    assertEquals(BuildingModeModel.ROWS, grid.length);
	    assertEquals(BuildingModeModel.COLUMNS, grid[0].length);
	    for (int[] row : grid) {
	        for (int cell : row) {
	            assertEquals(0, cell);
	        }
	    }
	}
	
	
	@Test
	//GB testing
	void testWriteAndReadTxt() throws IOException {
        String testFileName = "tempTestFile.txt";
        BuildingModeModel model = new BuildingModeModel(user);
        int[][] originalGrid = model.createEmptyGrid();
        originalGrid[0][1] = 1; // Intentionally setting a value to test read/write consistency

        model.writeTxt(testFileName, originalGrid);
        
        // Read the file back into a string and reconstruct the grid manually
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(testFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        }

        // Splitting the entire file content by new lines and then by spaces to reconstruct the grid
        String[] rows = fileContent.toString().split("\n");
        int[][] loadedGrid = new int[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].trim().split("\\s+");
            loadedGrid[i] = new int[values.length];
            for (int j = 0; j < values.length; j++) {
                loadedGrid[i][j] = Integer.parseInt(values[j]);
            }
        }

        // Assert that the manually parsed grid matches the original grid
        assertTrue(Arrays.deepEquals(originalGrid, loadedGrid), "Loaded grid should match saved grid.");

        // Clean up the test file
        new File(testFileName).delete();
    }

    
    
	@Test
	//GB testing
	public void testSaveGridToDatabase() {
	    int[][] grid = model.createEmptyGrid();
	    grid[5][5] = 3; // Set some values

	    model.saveGridToDatabase(grid);

	    // Verify by fetching data back from the database
	    try (Connection conn = DatabaseConnection.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement("SELECT grid FROM SavedGames WHERE username = ?")) {
	        pstmt.setString(1, model.getUser().getUsername());
	        ResultSet rs = pstmt.executeQuery();
	        assertTrue(rs.next());
	        String savedGridString = rs.getString(1);
	        assertNotNull(savedGridString);

	        // Further checks can be added to parse and verify the grid string
	    } catch (SQLException e) {
	        fail("Database access failed", e);
	    }
	}
	
	
    
    
    
}
