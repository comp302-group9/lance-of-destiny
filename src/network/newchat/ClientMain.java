package network.newchat;
// ClientMain.java

import javax.swing.*;

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
        
        new ClientController(model, view);
    }
}
