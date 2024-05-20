package ui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    private JPanel pausePanel;  // Panel for pause screen
    private JLabel pauseLabel;
    private JButton quitButton;
    private JButton saveButton;

    public RunningModeView(RunningModeModel model) {
        this.model = model;
        //setPreferredSize(new Dimension(WIDTH, HEIGHT));
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFocusable(true);  // Make the JPanel focusable
        requestFocusInWindow();
        setupUIComponents();
    }
    
    public void addQuitButtonListener(ActionListener listener) {
        quitButton.addActionListener(listener);
    }
    
    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
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
         
        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 18));
        quitButton.setForeground(Color.BLACK);
        topLeftPanel.add(quitButton);
        
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setForeground(Color.BLACK);
        topLeftPanel.add(saveButton);
        
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

            for (Box i: RunningModeModel.boxes){
                if (i!=null){
                i.draw(g);
                }
            }

            for (Barrier i: RunningModeModel.barriers){
                if (i!=null){
                i.draw(g);
                }
            }
        }

        

    }
}
