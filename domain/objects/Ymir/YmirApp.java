package domain.objects.Ymir;

import javax.swing.*;

public class YmirApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ymir UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600*16/9, 800);

        YmirModel model = new YmirModel();
        YmirView view = new YmirView();
        YmirController controller = new YmirController(model, view);

        frame.add(view);
        frame.setVisible(true);

        controller.start();
    }
}
