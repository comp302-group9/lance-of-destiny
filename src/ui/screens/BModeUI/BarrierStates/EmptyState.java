package ui.screens.BModeUI.BarrierStates;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierButton;

public class EmptyState implements BarrierState {
    @Override
    public void switchBarrier(BarrierButton button, BuildingModeModel model, int[][] grid) {
        button.setIcon(button.getSimpleIcon());
        model.setNumber_simple(model.getNumber_simple() + 1);
        grid[button.getRow()][button.getCol()] = 1;
        button.setState(new SimpleState());
    }
}