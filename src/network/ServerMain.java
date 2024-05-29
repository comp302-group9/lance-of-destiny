package network;

import javax.swing.*;

import domain.models.BuildingModeModel;
import ui.screens.BuildingModeView;

public class ServerMain {
    public static void main(String[] args) {
        ServerModel model = new ServerModel();
        ServerView view = new ServerView();
        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600*16/9, 600);
        frame.add(view);
        frame.setVisible(true);

        BuildingModeModel m =new BuildingModeModel(null);
        new ServerController(model, view, m, new BuildingModeView(m));
    }
}