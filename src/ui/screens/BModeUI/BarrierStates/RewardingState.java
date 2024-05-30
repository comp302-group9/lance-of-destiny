package ui.screens.BModeUI.BarrierStates;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierButton;

public class RewardingState implements BarrierState {
    @Override
    public void switchBarrier(BarrierButton button, BuildingModeModel model, int[][] grid) {
        button.setIcon(button.getEmptyIcon());
        model.setNumber_rewarding(model.getNumber_rewarding() - 1);
        grid[button.getRow()][button.getCol()] = 0;
        button.setState(new EmptyState());
    }
}