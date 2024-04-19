package ui.screens.BModeUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import domain.objects.Barrier.Barrier;

public class BarrierElement extends JPanel {
 
	JTextField textField = new JTextField();
	private JButton displayButton;

	public BarrierElement(Barrier b) {
		// setBackground(Color.BLACK); // Set the background color to black
		// setPreferredSize(new Dimension(500, 500)); // Set preferred size for the
		// panel

		// THIS IS CRITICAL IF YOU DONT PUT IT THEN IT MAY NOT APPEAR
		setLayout(null);
		setBackground(new Color(0,0,0,0));

		JLabel titleLabel = new JLabel(b.getName());
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		titleLabel.setBounds(10, 10, 200, 20);
		titleLabel.setForeground(Color.CYAN);
		add(titleLabel);

		JLabel messageLabel = new JLabel(b.getMessage());
		messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
		messageLabel.setBounds(125, 80, 200, 20);
		messageLabel.setForeground(Color.RED);
		add(messageLabel);

		textField.setBounds(130, 55, 50, 20); // Set bounds (x, y, width, height) for the text field
		textField.setBorder(BorderFactory.createLineBorder(getForeground())); // Add border for visibility
		textField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for better visibility
		textField.setHorizontalAlignment(JTextField.CENTER);

		setBorder(new LineBorder(Color.BLACK, 2));
		add(textField);
		
		ImageIcon icon = new ImageIcon(getClass().getResource(b.getImg())); // Adjust the path as necessary
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(20, 40, 100, 50); // Adjust size and position as needed
        add(imageLabel);

	}

	public String getTextFieldText() {
		return textField.getText();
	}

}
