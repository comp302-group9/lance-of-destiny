package ui.screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import domain.models.RunningModeModel;
import domain.objects.Box;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;

public class RunningModeView extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private RunningModeModel model;
    private BufferedImage backgroundImage;
    private BufferedImage heartImage; // Add this line to load the heart image
    private JPanel pausePanel;  // Panel for pause screen
    private JLabel pauseLabel;

    public RunningModeView(RunningModeModel model) {
        this.model = model;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
            heartImage = ImageIO.read(getClass().getResource("/ui/images/heart.png")); // Load the heart image
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFocusable(true);  // Make the JPanel focusable
        requestFocusInWindow();
        setupUIComponents();
    }

    private void setupUIComponents() {
        setLayout(new BorderLayout());

        pauseLabel = new JLabel("Pause");
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pauseLabel.setForeground(Color.WHITE);
        pauseLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setPaused(true);
                model.setPaused(true);
            }
        });

        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setOpaque(false);
        topLeftPanel.add(pauseLabel);
        add(topLeftPanel, BorderLayout.NORTH);
    }

    public void updateChances() {
        // No need to update text label anymore, handled in paintComponent
    }

    // Method to toggle the pause screen
    public void setPaused(boolean paused) {
        if (paused) {
            addPauseScreen();
        } else {
            model.setPaused(false);
            removePauseScreen();
        }
        revalidate();
        repaint();
    }

    private void addPauseScreen() {
        if (pausePanel == null) {
            pausePanel = new JPanel(new GridBagLayout());
            pausePanel.setBounds(0, 0, WIDTH, HEIGHT);
            pausePanel.setOpaque(false);

            JLabel pauseLabel = new JLabel("Game is paused");
            pauseLabel.setFont(new Font("Arial", Font.BOLD, 22));
            pauseLabel.setForeground(Color.WHITE);

            JButton resumeButton = new JButton("Resume");
            resumeButton.addActionListener(e -> setPaused(false));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            pausePanel.add(pauseLabel, gbc);
            pausePanel.add(resumeButton, gbc);
            add(pausePanel, BorderLayout.CENTER);
        }
    }

    private void removePauseScreen() {
        if (pausePanel != null) {
            remove(pausePanel);
            pausePanel = null;
        }
    }
    /**
    * Requires: A valid Graphics object `g` that is not null.
    * Modifies: This method modifies the visual output of the `RunningModeView` JPanel.
    * Effects: 
    *  - Draws the background image on the JPanel.
    *  - If the game is over, displays a "Game Over" message centered on the JPanel.
    *  - If the game is not over, it draws the paddle, fireball, boxes, and barriers.
    *  - Displays the number of lives left as heart images on the top right corner of the JPanel.
    */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        
        if (model.isGameOver()) { // Check if the game is over
            Font font = new Font("Arial", Font.BOLD, 36); // Font for game-over message
            g.setFont(font);
            g.setColor(Color.WHITE); // Red color for emphasis
            String gameOverMessage = model.gameOverMessage(); // Game over message text
            FontMetrics metrics = g.getFontMetrics(font); // Font metrics for centering
            int textWidth = metrics.stringWidth(gameOverMessage);
            int textHeight = metrics.getAscent(); // Ascent for text height
            g.drawString(gameOverMessage, (WIDTH - textWidth) / 2, (HEIGHT - textHeight) / 2); // Centered text
                        
        } else {
            Paddle paddle = model.getPaddle();
            paddle.draw(g);

            Fireball fireball = model.getFireball();
            fireball.draw(g);

            for (Box i : RunningModeModel.boxes) {
                if (i != null) {
                    i.draw(g);
                }
            }

            for (Barrier i : RunningModeModel.barriers) {
                if (i != null) {
                    i.draw(g);
                }
            }

            // Draw the hearts for lives
            int lives = model.getChances();
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            g.drawString("Lives:", WIDTH - 150, 30); // Adjust the position as needed
            for (int i = 0; i < lives; i++) {
                g.drawImage(heartImage, WIDTH - 80 + i * 25, 10, 20, 20, this);
            }
        }
    }
}

