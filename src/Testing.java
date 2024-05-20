
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import database.DatabaseConnection;
import domain.models.BuildingModeModel;
import domain.models.GameSession;
import domain.models.User;
import ui.screens.BuildingModeView;

public class Testing {
	
	private User user = new User("test1", "123", "123");
	private GameSession session;
	private BuildingModeModel model = new BuildingModeModel(user);
	private BuildingModeView view = new BuildingModeView(model);
	private int[][] testGrid;
	
	
//	@Test
//	void testBarrierInitialization() {
//	    assertEquals(4, model.getBarrierList().size(), "Should have 4 barriers initialized.");
//	}
//	
//	@Test
//	//BB testing
//    public void testGameSessionInitialization() {
//        session = new GameSession(user, 1, 3, 100, "0 1 2 3");
//
//        assertEquals(1, session.getGameId());
//        assertEquals(3, session.getLife());
//        assertEquals(100, session.getScore());
//        assertEquals("0 1 2 3", session.getGrid());
//    }
	
	
	@Test
	//GB Testing
	void testValidateBarriersNullOrNegativeValue() {
	    assertFalse(model.validateBarriers(), "Validation should fail because the numbers for barriers is not set.");
	    model.setNumber_simple(-75);
	    assertFalse(model.validateBarriers(), "Validation should fail because the number is negative");
	}
	
	
	@Test
	//GB Testing
	void testValidateBarriersNotEnoughSimpleBarriers() {
	    //model.setNumber_simple(5);
	    model.setNumber_reinforced(10);
	    model.setNumber_explosive(5);
	    model.setNumber_rewarding(10);
	    assertFalse(model.validateBarriers(), "Validation should fail because simple barriers are not defined.");
	    model.setNumber_simple(5);
	    assertFalse(model.validateBarriers(), "Validation should fail because not enough simple barriers.");
	    model.setNumber_simple(5);
	    assertFalse(model.validateBarriers(), "Validation should pass with correct counts.");
	}
	
	@Test
	//GB Testing
	void testValidateBarriersCorrectNoOfBarriers() {
	    model.setNumber_simple(75);
	    model.setNumber_reinforced(10);
	    model.setNumber_explosive(5);
	    model.setNumber_rewarding(10);
	    assertTrue(model.validateBarriers(), "Validation should pass with correct counts.");
	}
	
	@Test
	//GB Testing
	void testValidateBarriersTheBoundryConditions() {
	    model.setNumber_simple(75);
	    model.setNumber_reinforced(10);
	    model.setNumber_explosive(5);
	    model.setNumber_rewarding(129);
	    assertTrue(model.validateBarriers(), "Validation should pass with correct counts.");
	    model.setNumber_rewarding(130);
	    assertFalse(model.validateBarriers(), "Validation should fail because there is 1 more than it was supposed to");
	}
	
	@Test
	//GB Testing
	void testValidateBarriersBarrierNumberExceeded() {
	    // Assuming initial counts are all zero
	    model.setNumber_simple(75);
	    model.setNumber_reinforced(10);
	    model.setNumber_explosive(5);
	    model.setNumber_rewarding(1000);
	    assertFalse(model.validateBarriers(), "Validation should fail because of exceeding values.");
	}
	
	
	
	//The ones that are related with database:
	

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
	void testNullGridData() {
	    int[][] nullGrid = null;
	    assertThrows(NullPointerException.class, 
	                 () -> model.saveGridToDatabase(nullGrid),
	                 "Saving null grid should throw NullPointerException");
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
	    } finally {
	        // Cleanup: delete the test data
	        deleteTestData(model.getUser().getUsername());
	    }
	}
	
	
	@Test
    void testReadGrid() {
		int[][] testGrid = new int[BuildingModeModel.ROWS][BuildingModeModel.COLUMNS];
	    for (int i = 0; i < BuildingModeModel.ROWS; i++) {
	        for (int j = 0; j < BuildingModeModel.COLUMNS; j++) {
	            testGrid[i][j] = 0; // Default to empty
	        }
	    }

	    // Manually set specific values for testing
	    testGrid[0][0] = 1; // Simple
	    testGrid[0][1] = 2; // Firm
	    testGrid[0][2] = 0; // Empty
	    testGrid[0][3] = 4; // Rewarding

	    view.readGrid(testGrid); // Call the method

	    // Assertions
	    assertEquals(view.getSimple(), view.getIconOfButtonAt(0, 0));
	    assertEquals(view.getFirm(), view.getIconOfButtonAt(0, 1));
	    assertEquals(view.getEmpty(), view.getIconOfButtonAt(0, 2));
	    assertEquals(view.getRewarding(), view.getIconOfButtonAt(0, 3));
	    // Add assertions for each button to cover all cases.
    }
	
	
	
	
	
	
	
	
//	@Test
//	//GB testing
//	void testWriteAndReadTxt() throws IOException {
//        String testFileName = "tempTestFile.txt";
//        BuildingModeModel model = new BuildingModeModel(user);
//        int[][] originalGrid = model.createEmptyGrid();
//        originalGrid[0][1] = 1; // Intentionally setting a value to test read/write consistency
//
//        model.writeTxt(testFileName, originalGrid);
//        
//        // Read the file back into a string and reconstruct the grid manually
//        StringBuilder fileContent = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new FileReader(testFileName))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                fileContent.append(line).append("\n");
//            }
//        }
//
//        // Splitting the entire file content by new lines and then by spaces to reconstruct the grid
//        String[] rows = fileContent.toString().split("\n");
//        int[][] loadedGrid = new int[rows.length][];
//        for (int i = 0; i < rows.length; i++) {
//            String[] values = rows[i].trim().split("\\s+");
//            loadedGrid[i] = new int[values.length];
//            for (int j = 0; j < values.length; j++) {
//                loadedGrid[i][j] = Integer.parseInt(values[j]);
//            }
//        }
//
//        // Assert that the manually parsed grid matches the original grid
//        assertTrue(Arrays.deepEquals(originalGrid, loadedGrid), "Loaded grid should match saved grid.");
//
//        // Clean up the test file
//        new File(testFileName).delete();
//    }

    
    
	
	
	private void deleteTestData(String username) {
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement("DELETE FROM SavedGames WHERE username = ?")) {
	        pstmt.setString(1, username);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println("Failed to delete test data: " + e.getMessage());
	    }
	}
	
	
	
	
	
    
    
    
}
