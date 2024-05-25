package ui.screens;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
    private JButton quitButton;
    private JButton saveButton;
    private boolean gameStarted = false;
    private JLabel chancesLabel;
    private JLabel gifLabel;
    private BufferedImage heartImage;
    private JButton resumeButton;
    
    // Ymir components
    private YmirModel ymirModel;
    private YmirView ymirView;
    private YmirController ymirController;

    public RunningModeView(RunningModeModel model) {
        this.model = model;
        //setPreferredSize(new Dimension(DEFAULT.screenWidth, DEFAULT.screenHeight));
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
            heartImage = ImageIO.read(getClass().getResource("/ui/images/Heart.png"));
            gifIcon = new ImageIcon(getClass().getResource("/ui/gifs/W.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFocusable(true);  // Make the JPanel focusable
        requestFocusInWindow();
        setupUIComponents();
        setupYmirComponents(); 
    }

    public void addQuitButtonListener(ActionListener listener) {
        quitButton.addActionListener(listener);
    }

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    private void setupUIComponents() {
        setLayout(new BorderLayout());  // buralarda iki branch arası farklılıklar var sıkıntı çıkabilir

        // Create a single panel for the top with a border layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Create a panel for the left buttons and label
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setOpaque(false);

        JButton pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 18));
        pauseButton.setForeground(Color.BLACK); // You can set this to any color you prefer

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPaused(true);
                model.setPaused(true);
            }
        });

        topLeftPanel.add(pauseButton);

        topLeftPanel.setBounds(0, 0, 100, 30); // Adjust the size and position
        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 18));
        quitButton.setForeground(Color.BLACK);
        topLeftPanel.add(quitButton);

        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setForeground(Color.BLACK);
        topLeftPanel.add(saveButton);
        
        //add(topLeftPanel);
        topPanel.add(topLeftPanel, BorderLayout.WEST);

        // Add the chances label to the right
        chancesLabel = new JLabel("Chances: " + model.getChances());
        chancesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        chancesLabel.setForeground(Color.WHITE);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        topRightPanel.add(chancesLabel);

        topPanel.add(topRightPanel, BorderLayout.EAST);

        // Add the top panel to the frame
        add(topPanel, BorderLayout.NORTH);

        gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(WIDTH / 2 - gifIcon.getIconWidth() / 2, HEIGHT / 2 - gifIcon.getIconHeight() / 2, gifIcon.getIconWidth(), gifIcon.getIconHeight());
        add(gifLabel);
    }

    public void updateChances() {
        chancesLabel.setText("Chances: " + model.getChances());
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

            resumeButton = new JButton("Resume");
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

            // Draw the hearts for lives
            int lives = model.getChances();
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            // Position "Lives:" label
            int livesLabelX = WIDTH - 150;
            g.drawString("Lives:", livesLabelX, 20);

            // Draw heart icons next to "Lives:" label
            int nextX = livesLabelX + g.getFontMetrics().stringWidth("Lives:") + 10;

            for (int i = 0; i < lives; i++) {
                g.drawImage(heartImage, nextX, 10, 20, 20, this);
            }

            for (int i = 0; i < RunningModeModel.spells.size(); i++) {
                SpellIcon SI = RunningModeModel.spells.get(i);
                SI.setBounds(10, HEIGHT - 120 - i * 65, 50, 50);
                add(SI);
            }

            // Draw the score next to the last heart icon
            nextX += 10; // Add some spacing after the last heart icon
            g.drawString("Score: " + model.getScore(), nextX, 20);
        }
    }
}
