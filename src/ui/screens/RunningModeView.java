package ui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.models.RunningModeModel;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;


public class RunningModeView extends JPanel {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;
    private RunningModeModel model;
    private BufferedImage backgroundImage;
    private JPanel pausePanel;  // Panel for pause screen
    private JLabel pauseLabel;

    public RunningModeView(RunningModeModel model) {
        this.model = model; 
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        Paddle paddle = model.getPaddle();
        paddle.draw(g);

        Fireball fireball = model.getFireball();
        fireball.draw(g);

        for (Barrier i: RunningModeModel.barriers){
            if (i!=null){
            i.draw(g);
            }
        }

    }
}