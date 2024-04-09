// Main.java
import javax.swing.*;

import domain.controllers.*;
import domain.models.*;
import ui.screens.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the model, view, and controller
            RunningModeModel model = new RunningModeModel();
            RunningModeView view = new RunningModeView(model);
            RunningModeController controller = new RunningModeController(model, view);

            BuildingModeModel model2 = new BuildingModeModel();
            BuildingModeView view2 = new BuildingModeView(model2);
            BuildingModeController controller2 = new BuildingModeController(model2, view2);
            // Create the main frame
            JFrame frame = new JFrame("Running Mode");
            frame.getContentPane().add(view);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null); // Center the frame

            // Start the game loop
            Thread gameThread = new Thread(controller);
            gameThread.start();
        });
    }
}
