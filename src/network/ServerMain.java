package network;

import javax.swing.*;

public class ServerMain {
    public static void main(String[] args) {
        ServerModel model = new ServerModel();
        ServerView view = new ServerView();
        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600*16/9, 600);
        frame.add(view);
        frame.setVisible(true);

        //new ServerController(model, view);
    }
}