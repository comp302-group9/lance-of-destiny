package testing;

import static org.junit.Assert.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.junit.Test;
import javax.imageio.ImageIO;
import java.io.IOException;
import domain.objects.Box;
import domain.models.RunningModeModel;

public class BoxTest {
	
	/*
	Overview:
	The Box class represents a Spell Box object in the game. It handles the box's position, size, movement, and drawing the box on the game screen.

	Abstract Function:
	
	A box with a specific position (x, y) on the game screen.
	A size defined by its width and height.
	A movement speed boxSpeed that determines how fast the box moves down the screen.
	An image representing the box.
	
	Representation Invariant:
	The position (x, y) should always be within the bounds of the game screen.
	The width and height should be positive values.
	The image should be successfully loaded.
	
	 */
	// see repOk method in Box class.
    // Test Initial Position and Size
	
    @Test
    public void testInitialPositionAndSize() {
        Box box = new Box(100, 100);
        assertEquals(100, box.getX());
        assertEquals(100, box.getY());
        assertEquals(45, box.getWidth());
        assertEquals(55, box.getHeight());
        assertTrue(box.repOk());
    }

    // Test Movement of the Box
    @Test
    public void testMove() {
        Box box = new Box(100, 100);
        box.move();
        assertEquals(102, box.getY()); // Since boxSpeed is 2
        assertTrue(box.repOk());
    }

    // Test Box Image Loading
    @Test
    public void testImageLoading() {
        Box box = new Box(100, 100);
        assertNotNull(box.getImage());
        assertTrue(box.repOk());
    }

    // Test Drawing the Box
    @Test
    public void testDraw() {
        Box box = new Box(100, 100);
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics g = testImage.getGraphics();
        box.draw(g);
        assertTrue(box.repOk());
        g.dispose();
    }

    // Test Box Position after Multiple Moves
    @Test
    public void testMultipleMoves() {
        Box box = new Box(100, 100);
        for (int i = 0; i < 10; i++) {
            box.move();
        }
        assertEquals(120, box.getY()); // Since boxSpeed is 2 and moved 10 times
        assertTrue(box.repOk());
    }

    // Test Box Goes Out of Bounds
    @Test
    public void testOutOfBounds() {
        Box box = new Box(100, RunningModeModel.HEIGHT - 1);
        box.move();
        assertFalse(box.repOk()); // Should be out of bounds after moving
    }    
}