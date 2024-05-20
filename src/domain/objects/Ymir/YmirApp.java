package domain.objects.Ymir;

import javax.swing.*;

import domain.controllers.YmirController;
import domain.models.YmirModel;
import ui.screens.RModeUI.YmirView;

public class YmirApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ymir UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);

        YmirModel model = new YmirModel();
        YmirView view = new YmirView();
        YmirController controller = new YmirController(model, view);

        frame.add(view);
        frame.setVisible(true);

        controller.start();
    }
}
