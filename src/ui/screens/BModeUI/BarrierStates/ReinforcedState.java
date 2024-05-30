package ui.screens.BModeUI.BarrierStates;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierButton;

public class ReinforcedState implements BarrierState {
    @Override
    public void switchBarrier(BarrierButton button, BuildingModeModel model, int[][] grid) {
        button.setIcon(button.getExplosiveIcon());
        model.setNumber_reinforced(model.getNumber_reinforced() - 1);
        model.setNumber_explosive(model.getNumber_explosive() + 1);
        grid[button.getRow()][button.getCol()] = 3;
        button.setState(new ExplosiveState());
    }
}
