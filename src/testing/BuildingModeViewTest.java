package testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Arrays;

import domain.models.BuildingModeModel;
import domain.controllers.MyMouseListener;
import ui.screens.BuildingModeView;
import ui.screens.BModeUI.BarrierButton;

public class BuildingModeViewTest {

    private BuildingModeView view;
    private BuildingModeModel model;

    @BeforeEach
    public void setUp() {
        model = new BuildingModeModel();
        view = new BuildingModeView(model);
    }

    @Test
    public void testButtonCount() {
        view.addEmptyButtons();
        int expectedButtonCount = BuildingModeModel.ROWS * BuildingModeModel.COLUMNS;
        int actualButtonCount = view.buttons.length;
        assertEquals(expectedButtonCount, actualButtonCount);
    }

    @Test
    public void testButtonPositioning() {
        view.addEmptyButtons();
        
        int xStart = view.HEIGHT / 32;
        int yStart = view.WIDTH / 32;
        int xGap = view.HEIGHT / 128;
        int yGap = view.WIDTH / 96;
        int buttonWidth = view.buttonWidth;
        int buttonHeight = view.buttonHeight;
        
        BarrierButton firstButton = (BarrierButton) BuildingModeView.buttons[0];
        assertEquals(new Rectangle(xStart, yStart, buttonWidth, buttonHeight), firstButton.getBounds());
        
        int lastIndex = BuildingModeModel.ROWS * BuildingModeModel.COLUMNS;
        BarrierButton lastButton = (BarrierButton) view.getComponent(lastIndex);
        int lastRow = BuildingModeModel.ROWS - 1;
        int lastCol = BuildingModeModel.COLUMNS - 1;
        int lastX = xStart + lastCol * (buttonWidth + xGap);
        int lastY = yStart + lastRow * (buttonHeight + yGap);
        assertEquals(new Rectangle(lastX, lastY, buttonWidth, buttonHeight), lastButton.getBounds());
    }

    @Test
    public void testButtonProperties() {
        view.addEmptyButtons();
        BarrierButton firstButton = (BarrierButton) view.buttons[0];
        assertFalse(firstButton.isFocusable());
        assertFalse(firstButton.isContentAreaFilled());
        assertFalse(firstButton.isBorderPainted());
    }

    @Test
    public void testButtonActionListener() {
        view.addEmptyButtons();
        BarrierButton firstButton = (BarrierButton) view.buttons[0];
        ActionListener[] listeners = firstButton.getActionListeners();
        assertEquals(1, listeners.length);
        assertTrue(listeners[0] instanceof ActionListener);
    }

    @Test
    public void testButtonMouseListener() {
        view.addEmptyButtons();
        BarrierButton firstButton = (BarrierButton) view.buttons[0];
        MouseListener[] listeners = firstButton.getMouseListeners();
        assertTrue(Arrays.stream(listeners).anyMatch(listener -> listener instanceof MyMouseListener));
    }
}
