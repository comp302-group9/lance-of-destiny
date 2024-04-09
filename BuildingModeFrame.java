
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
		// Create and set up the main window
		JFrame frame = new JFrame("Barrier Element");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600); // Set your preferred size
		frame.setLocationRelativeTo(null); // Center the frame on the screen

		// Set layout to null to allow manual positioning of the panel
		frame.setLayout(null);

		// Create an instance of BarrierElement JPanel
		// SimpleBarrier b = new SimpleBarrier(6, 9);

		int y = 50; // Initial x-coordinate for the first panel

		for (int i = 0; i < 4; i++) {
			BarrierElement barrierElement = new BarrierElement();

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
