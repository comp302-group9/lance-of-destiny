package ui.screens.BModeUI;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierStates.BarrierState;
import ui.screens.BModeUI.BarrierStates.EmptyState;

public class BarrierButton extends JButton {
    private int row;
    private int col;
    private BarrierState state;
    private ImageIcon emptyIcon, simpleIcon, firmIcon, explosiveIcon, rewardingIcon;

    public BarrierButton(int row, int col) {
        this.row = row;
        this.col = col;
        this.state = new EmptyState(); // Initial state
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setState(BarrierState state) {
        this.state = state;
    }

    public void switchBarrier(BuildingModeModel model, int[][] grid) {
        state.switchBarrier(this, model, grid);
    }

    public void setIcons(ImageIcon empty, ImageIcon simple, ImageIcon firm, ImageIcon explosive, ImageIcon rewarding) {
        this.emptyIcon = empty;
        this.simpleIcon = simple;
        this.firmIcon = firm;
        this.explosiveIcon = explosive;
        this.rewardingIcon = rewarding;
        setIcon(emptyIcon); // Set the initial icon
    }

    public ImageIcon getEmptyIcon() {
        return emptyIcon;
    }

    public ImageIcon getSimpleIcon() {
        return simpleIcon;
    }

    public ImageIcon getFirmIcon() {
        return firmIcon;
    }

    public ImageIcon getExplosiveIcon() {
        return explosiveIcon;
    }

    public ImageIcon getRewardingIcon() {
        return rewardingIcon;
    }
}
