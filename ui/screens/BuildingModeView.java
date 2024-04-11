package ui.screens;

import javax.swing.JLabel;
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

    public BuildingModeView(BuildingModeModel model){
        this.model=model;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        setBackground(Color.BLACK);

        grid = model.readTxt("/domain/txtData/Test.txt");
        placement = new BarrierPlacementPanel(model, grid);
        placement.setBounds(0, 82, BarrierPlacementPanel.WIDTH, BarrierPlacementPanel.HEIGHT);
        this.add(placement);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
