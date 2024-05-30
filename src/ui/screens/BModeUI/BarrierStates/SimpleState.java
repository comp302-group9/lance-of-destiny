package ui.screens.BModeUI.BarrierStates;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierButton;

public class SimpleState implements BarrierState {
    @Override
    public void switchBarrier(BarrierButton button, BuildingModeModel model, int[][] grid) {
        button.setIcon(button.getFirmIcon());
        model.setNumber_simple(model.getNumber_simple() - 1);
        model.setNumber_reinforced(model.getNumber_reinforced() + 1);
        grid[button.getRow()][button.getCol()] = 2;
        button.setState(new ReinforcedState());
    }
}