package ui.screens;

import javax.swing.*;

import domain.controllers.BuildingModeController;
import domain.models.BuildingModeModel;
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
    private JButton backToBuildingModeButton;

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

            backToBuildingModeButton = new JButton("Turn Back To Building Mode");
            backToBuildingModeButton.addActionListener(e -> switchToBuildingMode());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            pausePanel.add(pauseLabel, gbc);
            pausePanel.add(pauseButton, gbc);
            pausePanel.add(saveButton, gbc);
            pausePanel.add(loadButton, gbc);
            pausePanel.add(quitButton, gbc);
            pausePanel.add(backToBuildingModeButton, gbc);
            add(pausePanel, BorderLayout.CENTER);
        }
    }

    private void switchToBuildingMode() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        BuildingModeModel buildingModel = new BuildingModeModel();
        BuildingModeView buildingView = new BuildingModeView(buildingModel);
        BuildingModeController buildingController = new BuildingModeController(buildingModel, buildingView);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(buildingView);
        frame.revalidate();
        frame.repaint();

        Thread buildingModeThread = new Thread(buildingController);
        buildingModeThread.start();
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

        for (domain.objects.Box box : RunningModeModel.boxes) {
            if (box != null) {
                box.draw(g);
            }
        }

        for (Barrier barrier : RunningModeModel.barriers) {
            if (barrier != null) {
                barrier.draw(g);
            }
        }

        // Draw the lives and score
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
            nextX += 25; // Move to the next position for the next heart icon
        }

        // Draw the score next to the last heart icon
        nextX += 10; // Add some spacing after the last heart icon
        g.drawString("Score: " + model.getScore(), nextX, 20);
    }
}
}

