package ui.screens;

import javax.swing.JPanel;

import domain.models.BuildingModeModel;
import ui.screens.BModeUI.BarrierPlacementPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BuildingModeView extends JPanel{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private BufferedImage backgroundImage;
    private BuildingModeModel model;
    private BarrierPlacementPanel placement;
    public int[][] grid;
    private int number_simple = 0;
    private int number_reinforced = 0;
    private int number_explosive = 0;
    private int number_rewarding = 0;

    public BuildingModeView(BuildingModeModel model){
        this.model=model;
        grid = model.readTxt("/domain/txtData/Test.txt");

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        setBackground(Color.BLACK);

        placement = new BarrierPlacementPanel();
        placement.setBounds(0, 82, BarrierPlacementPanel.WIDTH, BarrierPlacementPanel.HEIGHT);
        this.add(placement);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    protected void readGrid(int[][] grid){
        for (int i=0; i<10 ;i++){
            for (int j=0; j<11 ;j++){
                if(grid[i][j]!=0){
                switch (grid[i][j]) {
                    case 1:
                        number_simple++;
                        break;
                    case 2:
                        number_reinforced++;
                        break;
                    case 3:
                        number_explosive++;
                        break;
                    case 4:
                        number_rewarding++;
                        break;
                    }
                }
            }
        }
    }
}
