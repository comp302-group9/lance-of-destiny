
import java.awt.Color;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	RunningModeController controller;

	GameFrame() { // costruttore

		controller = new RunningModeController();

		// this.add(panel); //finestra AWT

		this.getContentPane().add(controller); // finestra Swing
		this.setTitle("Bricks Crusher: Break the Bricks");
		this.setResizable(false);
		this.setBackground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);

	} // end costruttore

}
