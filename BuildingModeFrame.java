
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import domain.Barrier;
import ui.BarrierElement;

public class BuildingModeFrame {

	BuildingModeFrame() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {

		BuildingModeModel bModel = new BuildingModeModel();
		ArrayList<Barrier> bList = bModel.getBarrierList();

		// Create and set up the main window
		JFrame frame = new JFrame("Barrier Element");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600); // Set your preferred size
		frame.setLocationRelativeTo(null); // Center the frame on the screen

		// Set layout to null to allow manual positioning of the panel
		frame.setLayout(null);

		int y = 50; // Initial x-coordinate for the first panel

		for (int i = 0; i < 4; i++) {

			BarrierElement barrierElement = new BarrierElement(bList.get(i));

			// Position the panel within the frame
			barrierElement.setBounds(550, y, 200, 100);

			// Add the BarrierElement JPanel to the main window
			frame.add(barrierElement);

			y += 120; // Increase x-coordinate for the next panel, with a gap of 20 pixels
		}

		// Display the window
		frame.setVisible(true);
	}

}
