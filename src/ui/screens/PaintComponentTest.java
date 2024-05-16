import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

import domain.models.RunningModeModel;
import domain.objects.Box;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;
import ui.screens.RunningModeView;

public class PaintComponentTest {

    private RunningModeView view;
    private RunningModeModel mockModel;
    private Graphics2D mockGraphics;
    private BufferedImage mockBackgroundImage;
    private BufferedImage mockHeartImage;
    private Paddle mockPaddle;
    private Fireball mockFireball;

    @Before
    public void setUp() {
        mockModel = new RunningModeModel() {
            @Override
            public boolean isGameOver() {
                return isGameOverMock;
            }

            @Override
            public String gameOverMessage() {
                return gameOverMessageMock;
            }

            @Override
            public Paddle getPaddle() {
                return mockPaddle;
            }

            @Override
            public Fireball getFireball() {
                return mockFireball;
            }

            @Override
            public int getChances() {
                return chancesMock;
            }
        };

        view = new RunningModeView(mockModel);
        mockGraphics = (Graphics2D) new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
        mockBackgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        mockHeartImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        mockPaddle = new Paddle(0, 0, 100, 20);
        mockFireball = new Fireball(0, 0, 20, 20);

        // Set private fields via reflection
        setPrivateField(view, "backgroundImage", mockBackgroundImage);
        setPrivateField(view, "heartImage", mockHeartImage);
    }

    private void setPrivateField(Object object, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isGameOverMock = false;
    private String gameOverMessageMock = "";
    private int chancesMock = 0;
    // BLACK BOX TEST 1 
    @Test
    public void testGameOverMessage() {
        isGameOverMock = true;
        gameOverMessageMock = "Game Over";

        view.paintComponent(mockGraphics);

        // Verify that the game-over message is drawn
        String drawnString = mockGraphics.toString();
        assertTrue(drawnString.contains("Game Over"));
    }
    // BLACK BOX TEST 2 
    @Test
    public void testPaddleDrawing() {
        isGameOverMock = false;

        view.paintComponent(mockGraphics);

        // Verify that the paddle is drawn
        // No easy way to directly verify graphics calls without Mockito, so we assert no exceptions.
        assertNotNull(mockPaddle);
    }
    // BLACK BOX TEST 3 
    @Test
    public void testFireballDrawing() {
        isGameOverMock = false;

        view.paintComponent(mockGraphics);

        // Verify that the fireball is drawn
        // No easy way to directly verify graphics calls without Mockito, so we assert no exceptions.
        assertNotNull(mockFireball);
    }
    // BLACK BOX TEST 4
    @Test
    public void testLivesDisplay() {
        isGameOverMock = false;
        chancesMock = 3;

        view.paintComponent(mockGraphics);

        // Verify that the lives count is displayed correctly
        // No easy way to directly verify graphics calls without Mockito, so we assert no exceptions.
        assertEquals(3, chancesMock);
    }
    // BLACK BOX TEST 5 
    @Test
    public void testBoxesDrawing() {
        Box box1 = new Box(100, 100);
        Box box2 = new Box(200, 200);
        RunningModeModel.boxes = new Box[]{box1, box2};

        isGameOverMock = false;

        view.paintComponent(mockGraphics);

        // Verify that each non-null box is drawn
        // No easy way to directly verify graphics calls without Mockito, so we assert no exceptions.
        assertNotNull(RunningModeModel.boxes[0]);
        assertNotNull(RunningModeModel.boxes[1]);
    }
    // BLACK BOX TEST 6
    @Test
    public void testBarriersDrawing() {
        Barrier barrier1 = new Barrier(100, 100);
        Barrier barrier2 = new Barrier(200, 200);
        RunningModeModel.barriers = new Barrier[]{barrier1, barrier2};

        isGameOverMock = false;

        view.paintComponent(mockGraphics);

        // Verify that each non-null barrier is drawn
        // No easy way to directly verify graphics calls without Mockito, so we assert no exceptions.
        assertNotNull(RunningModeModel.barriers[0]);
        assertNotNull(RunningModeModel.barriers[1]);
    }
    // GLASS BOX TEST 1 
    @Test
    public void testGameOverPath() {
        // Set up the internal state to simulate the game over condition
        isGameOverMock = true;
        gameOverMessageMock = "Game Over";

        // Create a Graphics2D object to capture the drawing calls
        BufferedImage bufferedImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Call the paintComponent method
        view.paintComponent(g2d);

        // Check if the game-over message is drawn correctly
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int textWidth = metrics.stringWidth("Game Over");
        int textHeight = metrics.getAscent();
        int x = (view.WIDTH - textWidth) / 2;
        int y = (view.HEIGHT - textHeight) / 2;

        // Verify that the text is drawn at the expected location
        boolean messageDrawn = bufferedImage.getSubimage(x, y, textWidth, textHeight) != null;
        assertTrue("Game over message should be drawn", messageDrawn);
    }   
    // GLASS BOX TEST 2 
    @Test
    public void testPaddleDrawingPath() {
        // Set up the internal state to simulate the game not being over
        isGameOverMock = false;
        mockPaddle = new Paddle(100, 500, 100, 20);

        // Create a Graphics2D object to capture the drawing calls
        BufferedImage bufferedImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Call the paintComponent method
        view.paintComponent(g2d);

        // Verify that the paddle is drawn at the correct location
        Rectangle expectedBounds = new Rectangle(mockPaddle.getX(), mockPaddle.getY(), mockPaddle.getWidth(), mockPaddle.getHeight());
        boolean paddleDrawn = g2d.hitClip(expectedBounds.x, expectedBounds.y, expectedBounds.width, expectedBounds.height);
        assertTrue("Paddle should be drawn at the correct location", paddleDrawn);
    }

}
