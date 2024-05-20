package testing;





import javax.swing.*;
import domain.controllers.*;
import ui.screens.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** private void signIn() {
    // method implementation
}
 *REQUIRES: A valid SignInView instance and DatabaseConnection should be properly set up.
 * MODIFIES: The status label in SignInView, initiates the BuildingModeView on successful sign-in.
 * EFFECTS: 
 *  - Retrieves the username and password from SignInView.
 *  - Attempts to validate the credentials against the database.
 *  - If the credentials are valid, updates the status label to "Sign in successful", closes the sign-in view,
 *    and opens the BuildingModeView.//FOR GLASS BOX TESTING test validateCredentials()
 *  - If the credentials are invalid, updates the status label to "Invalid credentials".
 *  - In case of a database error, updates the status label to "Database error: " followed by the error message.
 ** 
 *
 *private boolean validateCredentials(String username, String password, Connection conn) throws SQLException {
    // method implementation
}
 *Requires: A non-null, non-empty username and password, a valid SQL Connection.
 * Modifies: None.
 * Effects: 
 *  - Prepares an SQL statement to fetch the password for the given username.
 *  - Executes the statement and retrieves the password from the result set.
 *  - Returns true if the provided password matches the stored password; false otherwise.
 *  - Throws SQLException if there is a database access error.
 */



public class SignInSignUpTest {

    private SignInController signInController;
    private SignInView signInView;
    private MockConnection mockConnection;
    private MockPreparedStatement mockStatement;
    private MockResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        // Create a new instance of SignInView
        signInView = new SignInView();
        
        // Create a new instance of SignInController with the SignInView
        signInController = new SignInController(signInView);

        // Set up the database connection objects manually
        mockConnection = new MockConnection();
        mockStatement =  new MockPreparedStatement();
        mockResultSet = new MockResultSet();
    }

    @Test
    void testSignInSuccessful() throws SQLException {
        // Set up the mock objects
        signInView.setUsername("testUser");
        signInView.setPassword("testPass");

        // Stub the database interaction methods
        mockConnection.setPreparedStatement(mockStatement);
        mockStatement.setResultSet(mockResultSet);
         mockResultSet.setNext(true);
         mockResultSet.setPassword("testPass");

        // Call the signIn method
        JButton signInButton = signInView.getSignInButton();
        signInButton.doClick();

        // Verify the interactions and assertions
        assertEquals("Sign in successful.", signInView.getSignInStatus());
        assertFalse(signInView.getFrame().isVisible());

        
    }

    @Test
   void testSignInInvalidCredentials() throws SQLException {
        // Set up the mock objects
        signInView.setUsername("testUser");
        signInView.setPassword("wrongPass");

        // Stub the database interaction methods
         mockConnection.setPreparedStatement(mockStatement);
         mockStatement.setResultSet(mockResultSet);
         mockResultSet.setNext(true);
         mockResultSet.setPassword("testPass");

        // Call the signIn method
        // Call the signIn method
        JButton signInButton = signInView.getSignInButton();
        signInButton.doClick();

        // Verify the interactions and assertions
        assertEquals("Invalid credentials.", signInView.getSignInStatus());
        assertTrue(signInView.getFrame().isVisible());
    }

    @Test
     void testSignInDatabaseError() throws SQLException {
        // Set up the mock objects to throw a SQLException
        signInView.setUsername("testUser");
        signInView.setPassword("testPass");

        // Stub the database interaction methods
         mockConnection.setThrowSQLException(true);

        // Call the signIn method
        // Call the signIn method
        JButton signInButton = signInView.getSignInButton();
        signInButton.doClick();

        // Verify the interactions and assertions
        assertTrue(signInView.getSignInStatus().startsWith("Database error:"));
        assertTrue(signInView.getFrame().isVisible());
    }

    @Test
    void testShowSignUpView() {
    	 // Call the signIn method
        JButton signUpButton = signInView.getSignUpButton();
        signUpButton.doClick();
        

        // Verify that the frame was closed
        assertFalse(signInView.getFrame().isVisible());
    }
    @Test
    void testSQLInjection() throws SQLException {
        signInView.getUsernameField().setText("' OR '1'='1");
        signInView.getPasswordField().setText("anyPass");

        mockResultSet.setNext(true); // Simulate user found
         mockResultSet.setPassword("anyPass"); // Simulate password matching

        triggerSignInButtonAction();

        assertEquals("Invalid credentials.", signInView.getStatusLabel().getText());
    }
    private void triggerSignInButtonAction() {
        for (ActionListener al : signInView.getSignInButton().getActionListeners()) {
            al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }
    @Test
    void testNoUserFound() throws SQLException {
        signInView.getUsernameField().setText("unknownUser");
        signInView.getPasswordField().setText("anyPass");

         mockResultSet.setNext(false); // Simulate user not found

        triggerSignInButtonAction();

        assertEquals("Invalid credentials.", signInView.getStatusLabel().getText());
    }
}

class MockConnection {
    private MockPreparedStatement preparedStatement;
    private boolean throwSQLException;
    
    public MockConnection() {
        // Default constructor
    }

    
    public void setPreparedStatement(MockPreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public void setThrowSQLException(boolean throwSQLException) {
        this.throwSQLException = throwSQLException;
    }

    public MockPreparedStatement prepareStatement(String sql) throws SQLException {
        if (throwSQLException) {
            throw new SQLException("Database error");
        }
        return preparedStatement;
    }

    
}

class MockPreparedStatement  {
    private MockResultSet resultSet;
    public MockPreparedStatement() {
        // Default constructor
    }

    

    public void setResultSet(MockResultSet mockResultSet) {
        this.resultSet = mockResultSet;
    }

    public MockResultSet executeQuery() throws SQLException {
        return resultSet;
    }

    
}

class MockResultSet  {
    private boolean next;
    private String password;
    
    public MockResultSet() {
        // Default constructor
    }

    
    public void setNext(boolean next) {
        this.next = next;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean next() throws SQLException {
        return next;
    }

    public String getString(String columnLabel) throws SQLException {
        if ("password".equals(columnLabel)) {
            return password;
        }
        return null;
    }

    
}


