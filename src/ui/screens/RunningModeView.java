package ui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private BufferedImage heartImage;
    private JLabel chancesLabel;
    
    private JButton resumeButton;
    //private JButton saveButton1;
    //private JButton quitButton1;
    

    public RunningModeView(RunningModeModel model) {
        this.model = model;
        //setPreferredSize(new Dimension(WIDTH, HEIGHT));
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
            heartImage = ImageIO.read(getClass().getResource("/ui/images/Heart.png"));
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
    
//    private void initButtons() {
//        saveButton1 = new JButton("Save");
//        quitButton1 = new JButton("Quit");
//        // Optionally, add listeners here or ensure they're added only after this method
//    }

    private void setupUIComponents() {
        setLayout(new BorderLayout());
        
        // Create a single panel for the top with a border layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Create a panel for the left buttons and label
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setOpaque(false);
        
//        pauseLabel = new JLabel("Pause");
//        pauseLabel.setFont(new Font("Arial", Font.BOLD, 18));
//        pauseLabel.setForeground(Color.WHITE);
//        pauseLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        pauseLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                setPaused(true);
//                model.setPaused(true);
//            }
//        });
//        topLeftPanel.add(pauseLabel);
        
        JButton pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 18));
        pauseButton.setForeground(Color.BLACK); // You can set this to any color you prefer

        // Add an ActionListener to handle button clicks
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPaused(true);
                model.setPaused(true);
            }

        });

        // Add the pause button to the topLeftPanel
        topLeftPanel.add(pauseButton);

        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 18));
        quitButton.setForeground(Color.BLACK);
        topLeftPanel.add(quitButton);
        
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setForeground(Color.BLACK);
        topLeftPanel.add(saveButton);
        
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
    }
    
    public void updateChances() {
        chancesLabel.setText("Chances: " + model.getChances());
    }
// Paint Component'da hallediliyor

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

            resumeButton = new JButton("Resume");
            resumeButton.addActionListener(e -> setPaused(false));
            
//            saveButton1 = new JButton("Save");
////            saveButton.addActionListener(e -> saveGame());
//            
//            quitButton1 = new JButton("Quit");
//            quitButton.addActionListener(e -> quitGame());
//            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            pausePanel.add(pauseLabel, gbc);
            pausePanel.add(resumeButton, gbc);
//            pausePanel.add(saveButton1, gbc);
//            pausePanel.add(quitButton1, gbc);
//            
            
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
