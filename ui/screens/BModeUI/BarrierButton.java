package ui.screens.BModeUI;
import javax.swing.JButton;

public class BarrierButton extends JButton {
    private int row;
    private int col;

    public BarrierButton(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { 
        return row;
    }

    public int getCol() {
        return col;
    }
}