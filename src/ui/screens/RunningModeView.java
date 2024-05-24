package ui.screens;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import domain.DEFAULT;
import domain.controllers.YmirController;
import domain.models.RunningModeModel;
import domain.models.YmirModel;
import domain.objects.Box;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;
import ui.screens.RModeUI.SpellIcon;
import ui.screens.RModeUI.YmirView;

public class RunningModeView extends JPanel {
    private int HEIGHT = DEFAULT.screenHeight;    
    private int WIDTH = DEFAULT.screenWidth;
    private RunningModeModel model;
    private BufferedImage backgroundImage;
    private ImageIcon gifIcon;
    private JPanel pausePanel;  // Panel for pause screen
    private JLabel pauseLabel;
    private boolean gameStarted = false;
    private JLabel gifLabel;
    
    // Ymir components
    private YmirModel ymirModel;
    private YmirView ymirView;
    private YmirController ymirController;

    public RunningModeView(RunningModeModel model) {
        this.model = model;
        setPreferredSize(new Dimension(DEFAULT.screenWidth, DEFAULT.screenHeight));
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
            gifIcon = new ImageIcon(getClass().getResource("/ui/gifs/W.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFocusable(true);  // Make the JPanel focusable
        requestFocusInWindow();
        setupUIComponents();
        setupYmirComponents(); 
    }

    private void setupUIComponents() {
        setLayout(null);

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
        topLeftPanel.setBounds(0, 0, 100, 30); // Adjust the size and position
        add(topLeftPanel);

        gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(WIDTH / 2 - gifIcon.getIconWidth() / 2, HEIGHT / 2 - gifIcon.getIconHeight() / 2, gifIcon.getIconWidth(), gifIcon.getIconHeight());
        add(gifLabel);
    }

    private void setupYmirComponents() {
        ymirModel = new YmirModel();
        ymirView = new YmirView();
        ymirController = new YmirController(ymirModel, ymirView);

        ymirView.setBounds(WIDTH - 127, HEIGHT - 90, 300, 200); // Adjust size and position as needed
        add(ymirView);

        ymirController.start();
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
            pausePanel.setBounds(0, 0, DEFAULT.screenWidth, DEFAULT.screenWidth);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        if (model.getFireball().isLaunched()) {
            gifLabel.setVisible(false);
        } else {
            gifLabel.setVisible(true);
        }

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

            for (int i = 0; i < RunningModeModel.spells.size(); i++) {
                SpellIcon SI = RunningModeModel.spells.get(i);
                SI.setBounds(10, HEIGHT - 120 - i * 65, 50, 50);
                add(SI);
            }
        }
    }
}
