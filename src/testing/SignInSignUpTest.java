package testing;



import javax.swing.*;
import domain.controllers.*;
import ui.screens.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class SignInSignUpTest {

    private SignInController signInController;
    private SignInView signInView;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        // Create a new instance of SignInView
        signInView = new SignInView();
        
        // Create a new instance of SignInController with the SignInView
        signInController = new SignInController(signInView);

        // Set up the database connection objects manually
        mockConnection = (Connection) new MockConnection();
        mockStatement = (PreparedStatement) new MockPreparedStatement();
        mockResultSet = (ResultSet) new MockResultSet();
    }

    @Test
    void testSignInSuccessful() throws SQLException {
        // Set up the mock objects
        signInView.setUsername("testUser");
        signInView.setPassword("testPass");

        // Stub the database interaction methods
        ((MockConnection) mockConnection).setPreparedStatement(mockStatement);
        ((MockPreparedStatement) mockStatement).setResultSet(mockResultSet);
        ((MockResultSet) mockResultSet).setNext(true);
        ((MockResultSet) mockResultSet).setPassword("testPass");

        // Call the signIn method
        JButton signInButton = signInView.getSignInButton();
        signInButton.doClick();

        // Verify the interactions and assertions
        assertEquals("Sign in successful.", signInView.getSignInStatus());
        assertFalse(signInView.getFrame().isVisible());

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
        ((MockConnection) mockConnection).setPreparedStatement(mockStatement);
        ((MockPreparedStatement) mockStatement).setResultSet(mockResultSet);
        ((MockResultSet) mockResultSet).setNext(true);
        ((MockResultSet) mockResultSet).setPassword("testPass");

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
        ((MockConnection) mockConnection).setThrowSQLException(true);

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
}

class MockConnection {
    private PreparedStatement preparedStatement;
    private boolean throwSQLException;

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public void setThrowSQLException(boolean throwSQLException) {
        this.throwSQLException = throwSQLException;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (throwSQLException) {
            throw new SQLException("Database error");
        }
        return preparedStatement;
    }

    // Implement other required methods from Connection as needed
}

class MockPreparedStatement  {
    private ResultSet resultSet;

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public ResultSet executeQuery() throws SQLException {
        return resultSet;
    }

    // Implement other required methods from PreparedStatement as needed
}

class MockResultSet  {
    private boolean next;
    private String password;

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

    // Implement other required methods from ResultSet as needed
}

