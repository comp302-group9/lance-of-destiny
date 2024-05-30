package ui.screens.BModeUI.BarrierStates;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierButton;

public class ExplosiveState implements BarrierState {
    @Override
    public void switchBarrier(BarrierButton button, BuildingModeModel model, int[][] grid) {
        button.setIcon(button.getRewardingIcon());
        model.setNumber_explosive(model.getNumber_explosive() - 1);
        model.setNumber_rewarding(model.getNumber_rewarding() + 1);
        grid[button.getRow()][button.getCol()] = 4;
        button.setState(new RewardingState());
    }
}
