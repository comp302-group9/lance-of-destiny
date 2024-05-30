package ui.screens.BModeUI.BarrierStates;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierButton;

public interface BarrierState {
    void switchBarrier(BarrierButton button, BuildingModeModel model, int[][] grid);
}