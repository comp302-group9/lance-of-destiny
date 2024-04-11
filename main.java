// Main.java
import javax.swing.*;

import domain.controllers.*;
import domain.models.*;
import ui.screens.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the model, view, and controller
            

            BuildingModeModel model2 = new BuildingModeModel();
            BuildingModeView view2 = new BuildingModeView(model2);
            BuildingModeController controller2 = new BuildingModeController(model2, view2);
            // Create the main frame
            JFrame frame = new JFrame("Running Mode");
            frame.getContentPane().add(view2);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null); // Center the frame

            // Start the game loop
        });
    }
}