// Main.java
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.screens.SignInPage;

public class main { 
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> {
            // Create the model, view, and controller
            
            //BuildingModeModel model2 = new BuildingModeModel();
            //BuildingModeView view2 = new BuildingModeView(model2);
            //BuildingModeController controller2 = new BuildingModeController(model2, view2);
            
            SignInPage view1 = new SignInPage();
            
            
            // Create the main frame
            JFrame frame = new JFrame("Building Mode");
            frame.getContentPane().add(view1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setSize(SignInPage.WIDTH, SignInPage.HEIGHT);
            frame.setLocationRelativeTo(null); // Center the frame
            frame.setResizable(false);

            // Start the game loop
        });
    }
}
