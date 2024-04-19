package ui.screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.models.RunningModeModel;
import domain.objects.Fireball;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;


public class RunningModeView extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private RunningModeModel model;
    private BufferedImage backgroundImage;
    private JPanel pausePanel;  // Panel for pause screen

    public RunningModeView(RunningModeModel model) {
        this.model = model;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to toggle the pause screen
    public void setPaused(boolean paused) {
        if (paused) {
            addPauseScreen();
        } else {
            removePauseScreen();
        }
        revalidate();
        repaint();
    }

    private void addPauseScreen() {
        pausePanel = new JPanel(new GridBagLayout());
        pausePanel.setBounds(0, 0, WIDTH, HEIGHT);
        pausePanel.setOpaque(false);

        JLabel pauseLabel = new JLabel("Game Paused");
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 48));
        pauseLabel.setForeground(Color.WHITE);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPaused(false);  // Remove pause screen when button is clicked
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        pausePanel.add(pauseLabel, gbc);
        pausePanel.add(resumeButton, gbc);
        add(pausePanel, BorderLayout.CENTER);
    }

    private void removePauseScreen() {
        remove(pausePanel);
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

        if (model.isPaused()) {
            g.setColor(new Color(0, 0, 0, 128)); // Translucent black overlay
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.drawString("Game Paused - Press 'P' to Resume", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }
}
