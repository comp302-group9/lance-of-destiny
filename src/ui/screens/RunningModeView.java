package ui.screens;

import javax.swing.*;

import domain.models.RunningModeModel;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RunningModeView extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private RunningModeModel model;
    private BufferedImage backgroundImage;
    private BufferedImage heartImage;
    private JPanel pausePanel;
    private JLabel pauseLabel;
    private JButton pauseButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton quitButton;

    public RunningModeView(RunningModeModel model) {
        this.model = model;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
            heartImage = ImageIO.read(getClass().getResource("/ui/images/heart.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFocusable(true);
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

            pauseButton = new JButton("Resume");
            pauseButton.addActionListener(e -> setPaused(false));

            saveButton = new JButton("Save");
            saveButton.addActionListener(e -> saveGame());

            loadButton = new JButton("Load");
            loadButton.addActionListener(e -> loadGame());

            quitButton = new JButton("Quit");
            quitButton.addActionListener(e -> quitGame());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            pausePanel.add(pauseLabel, gbc);
            pausePanel.add(pauseButton, gbc);
            pausePanel.add(saveButton, gbc);
            pausePanel.add(loadButton, gbc);
            pausePanel.add(quitButton, gbc);
            add(pausePanel, BorderLayout.CENTER);
        }
    }

    private void removePauseScreen() {
        if (pausePanel != null) {
            remove(pausePanel);
            pausePanel = null;
        }
    }

    private void saveGame() {
        // Implement save game logic here
    }

    private void loadGame() {
        // Implement load game logic here
    }

    private void quitGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        if (model.isGameOver()) {
            Font font = new Font("Arial", Font.BOLD, 36);
            g.setFont(font);
            g.setColor(Color.WHITE);
            String gameOverMessage = model.gameOverMessage();
            FontMetrics metrics = g.getFontMetrics(font);
            int textWidth = metrics.stringWidth(gameOverMessage);
            int textHeight = metrics.getAscent();
            g.drawString(gameOverMessage, (WIDTH - textWidth) / 2, (HEIGHT - textHeight) / 2);
        } else {
            Paddle paddle = model.getPaddle();
            paddle.draw(g);

            Fireball fireball = model.getFireball();
            fireball.draw(g);

            for (domain.objects.Box i : RunningModeModel.boxes) {
                if (i != null) {
                    i.draw(g);
                }
            }

            for (Barrier i : RunningModeModel.barriers) {
                if (i != null) {
                    i.draw(g);
                }
            }

            int lives = model.getChances();
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            g.drawString("Lives:", WIDTH - 150, 30);
            for (int i = 0; i < lives; i++) {
                g.drawImage(heartImage, WIDTH - 80 + i * 25, 10, 20, 20, this);
            }
        }
    }
}
