package ui.screens;

import javax.swing.*;
import java.awt.*;

public class HelpMenu extends JFrame {
    public HelpMenu() {
        setTitle("Help Menu");
        setSize(800, 600); // Set the size of the help window
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea helpText = new JTextArea();
        helpText.setText(getHelpText()); // Set the help content
        helpText.setEditable(false); // Make the text area read-only
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    private String getHelpText() {
        // Detailed help content
        return " Help Menu - Lance of Destiny\n\n" +
                " Introduction\n" +
                "Lance of Destiny is an exciting and competitive game where two brave warriors race to obtain the Lance of Fate, " +
                "a powerful artifact created by the arch-magician Fistandantalus. The lance grants its holder the ability to rule the world. " +
                "Players must overcome various barriers to prove their worthiness and reach the lance first.\n\n" +

                " Gameplay\n" +
                " Player Actions\n" +
                "- **Magical Staff (Paddle) Control**:\n" +
                "  - Use the left and right arrow keys to move the Magical Staff horizontally.\n" +
                "  - Press 'A' to rotate the staff 45 degrees to the left.\n" +
                "  - Press 'D' to rotate the staff 135 degrees to the right.\n" +
                "  - The staff will return to its horizontal position once the keys are released.\n\n" +
                "- **Fire Ball**:\n" +
                "  - The game starts with the Fire Ball on top of the Magical Staff.\n" +
                "  - Launch the Fire Ball by clicking the left mouse button or pressing 'W'.\n" +
                "  - Direct the Fire Ball to destroy barriers while preventing it from falling.\n\n" +
                "- **Spells**:\n" +
                "  - Collect spells during the game.\n" +
                "  - Click the spell icon or press the corresponding key to activate a spell.\n\n" +

                " Game Objects\n" +
                " Magical Staff\n" +
                "- Moves horizontally and can rotate to angle shots.\n" +
                "- Length is 10% of the screen width, and thickness is 20 pixels.\n" +
                "- Movement speed varies with key presses.\n\n" +

                " Barriers\n" +
                "- **Simple Barrier (Ancient Wall)**: Breaks in one hit.\n" +
                "- **Reinforced Barrier (Triple Rashōmon)**: Requires multiple hits, indicated by a number on the barrier.\n" +
                "- **Explosive Barrier (Volatile Fence)**: Explodes when hit; falling debris can cause the player to lose a chance.\n" +
                "- **Rewarding Barrier (Wish Endower)**: Breaks in one hit and drops a box containing a spell.\n\n" +

                " Fire Ball\n" +
                "- Size: 16x16 pixels.\n" +
                "- Bounces between barriers and the Magical Staff.\n" +
                "- If missed, the player loses a chance.\n\n" +

                " Scoring\n" +
                "- Score is calculated based on the time taken to destroy barriers:\n" +
                "  newScore = oldScore + 300 / (currentTime - gameStartingTime)\n" +
                "- The score and the number of remaining chances are displayed on the screen.\n\n" +

                " Game Modes\n" +
                " Building Mode\n" +
                "- Start the game by creating or loading a game layout.\n" +
                "- Place barriers using mouse clicks.\n" +
                "- Minimum criteria:\n" +
                "  - At least 75 simple barriers.\n" +
                "  - At least 10 reinforced barriers.\n" +
                "  - At least 5 explosive barriers.\n" +
                "  - At least 10 rewarding barriers.\n\n" +

                " Running Mode\n" +
                "- Control the Magical Staff to break barriers.\n" +
                "- The game can be paused, saved, loaded, or quit at any time.\n" +
                "- The current score and remaining chances are displayed.\n\n" +

                " Game Features\n" +
                "- **Save/Load**: Save the game state and load it to continue playing later.\n" +
                "- **Pause/Resume**: Pause the game and resume it later.\n" +
                "- **Help Screen**: Provides information about the game objects, features, and how to play.\n" +
                "- **Login Screen**: Players must log in to play, and can load only their saved games.\n\n" +

                " Versioning Support\n" +
                "- The game supports versioning for saved states to ensure compatibility with future updates.\n";
    }
}
