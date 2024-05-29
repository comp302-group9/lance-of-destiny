package network;
// ClientMain.java

import javax.swing.*;

import domain.models.BuildingModeModel;
import ui.screens.BuildingModeView;

public class ClientMain {
    public static void main(String[] args) {
        String serverAddress = JOptionPane.showInputDialog("Enter server IP address:");
        String clientName = JOptionPane.showInputDialog("Enter your name:");

        ClientModel model = new ClientModel(serverAddress, clientName);
        ClientView view = new ClientView();
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600*16/9, 600);
        frame.add(view);
        frame.setVisible(true);
        
        BuildingModeModel m=new BuildingModeModel(null);
        new ClientController(model, view, m, new BuildingModeView(m));
    }
}