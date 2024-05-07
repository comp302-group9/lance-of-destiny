package ui.screens.BModeUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import domain.models.GameSession;

public class GameElement extends JPanel {
	 
	JTextField textField = new JTextField();
	private JButton displayButton;

	public GameElement(GameSession b) {
		// setBackground(Color.BLACK); // Set the background color to black
		// setPreferredSize(new Dimension(500, 500)); // Set preferred size for the
		// panel

		// THIS IS CRITICAL IF YOU DONT PUT IT THEN IT MAY NOT APPEAR
		setLayout(null); 

		// Title Label
        JLabel titleLabel = new JLabel("Game1");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(10, 10, 200, 20);
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel);

        // Life Left Label
        JLabel lifeLeftLabel = new JLabel("Life left:");
        lifeLeftLabel.setFont(new Font("Arial", Font.BOLD, 12));
        lifeLeftLabel.setBounds(10, 50, 70, 20);
        lifeLeftLabel.setForeground(Color.RED);
        add(lifeLeftLabel);
        
        // Play Button
        JButton playButton = new JButton("Play");
        playButton.setBounds(130, 50, 70, 20);
        add(playButton);

        // Set border around the panel
        setBorder(new LineBorder(Color.BLACK, 2));
        //setPreferredSize(getPreferredSize()); // Set preferred size
	
	}

	public String getTextFieldText() {
		return textField.getText();
	}

}
