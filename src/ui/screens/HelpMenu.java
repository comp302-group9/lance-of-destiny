package ui.screens;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class HelpMenu extends JDialog {
    private BuildingModeView buildingModeView;

    public HelpMenu(BuildingModeView buildingModeView) {
        super((JFrame) SwingUtilities.getWindowAncestor(buildingModeView), "Help Menu", true);
        this.buildingModeView = buildingModeView;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(buildingModeView);

        JTextArea helpText = new JTextArea();
        helpText.setText(
                " WELCOME TO LANCE OF DESTINY GAME\n\n" +  
                "How to Play\n\n" +
                "In Lance of Destiny, two warriors race to obtain the Lance of Fate, a powerful artifact created by the arch-magician Fistandantalus. The first warrior to overcome all barriers and reach the Lance wins the game. Each warrior is equipped with a Magical Staff and a Fire Ball to navigate the challenges.\n\n" +
                "Controls\n\n" +
                "Move Magical Staff (Paddle):\n" +
                "          Left Arrow: Move left\n" +
                "          Right Arrow: Move right\n" +
                "          A Key: Rotate 45 degrees to the left\n" +
                "          D Key: Rotate 135 degrees to the right\n" +
                "Launch Fire Ball:\n" +
                "          Mouse Left Button or W Key\n" +
                "Activate Spells:\n" +
                "          Click on the spell icon or press the first letter of the spell's name\n\n" +
                "Tips for Success\n" +
                "           Utilize the rotation of the Magical Staff to aim the Fire Ball accurately.\n" +
                "           Collect and strategically use spells to gain an advantage.\n" +
                "           Keep an eye on the number of chances left and aim to preserve them.\n"
        );
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(helpText);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}