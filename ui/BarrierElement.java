package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import domain.Barrier;

public class BarrierElement extends JPanel {

	JTextField textField = new JTextField();
	private JButton displayButton;

	public BarrierElement(Barrier b) {
		// setBackground(Color.BLACK); // Set the background color to black
		// setPreferredSize(new Dimension(500, 500)); // Set preferred size for the
		// panel

		// THIS IS CRITICAL IF YOU DONT PUT IT THEN IT MAY NOT APPEAR
		setLayout(null);

		JLabel titleLabel = new JLabel(b.getName());
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		titleLabel.setBounds(10, 10, 200, 20);
		add(titleLabel);

		textField.setBounds(125, 50, 50, 30); // Set bounds (x, y, width, height) for the text field
		textField.setBorder(BorderFactory.createLineBorder(getForeground())); // Add border for visibility
		textField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for better visibility

		displayButton = new JButton("Display Text");
		displayButton.setBounds(120, 70, 150, 30);
		displayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Text Field Content: " + textField.getText());
			}
		});

		add(displayButton); // Add the display button to the panel
		setBorder(new LineBorder(Color.BLACK, 2));
		add(textField); // Add the text field to the panel

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw a rectangle shape
		g.setColor(Color.BLUE); // Set color of the shape
		g.fillRect(10, 40, 100, 50); // Fill rectangle shape
		g.setColor(Color.BLACK); // Set color of the outline
		g.drawRect(10, 40, 100, 50); // Draw rectangle outline

	}

	public String getTextFieldText() {
		return textField.getText();
	}

}
