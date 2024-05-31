package ui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.DEFAULT;
import domain.controllers.BuildingModeController;
import domain.controllers.RunningModeController;
import domain.controllers.YmirController;
import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import domain.models.YmirModel;
import domain.objects.Box;
import domain.objects.Fireball;
import domain.objects.ObjectFactory;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;
import domain.objects.Barrier.Debris;
import domain.objects.Barrier.HollowPurpleBarrier;
import network.Connectable;
import ui.screens.RModeUI.GameStatusPanel;
import ui.screens.RModeUI.SpellIcon;
import ui.screens.RModeUI.TopMenuPanel;
import ui.screens.RModeUI.YmirView;

public class RunningModeView extends JPanel {
    private int HEIGHT = DEFAULT.screenHeight;
    private int WIDTH = DEFAULT.screenWidth;
    private RunningModeModel model;
    private BufferedImage backgroundImage;
    private JPanel pausePanel;
    private JButton resumeButton;
    private JButton quitButton;
    private JButton saveButton;
    private JButton backToBuildingModeButton;
    private JLabel countdownLabel;
    private GameStatusPanel gameStatusPanel; // Declare GameStatusPanel
    private Connectable connectable;
    
    private YmirModel ymirModel;
    private YmirView ymirView;
    private YmirController ymirController;

    public RunningModeView(RunningModeModel model) {
        this.model = model;
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFocusable(true);
        requestFocusInWindow();
        setupUIComponents();
        setupYmirComponents();
        setupCallbacks();
    }

    public RunningModeView(RunningModeModel model, boolean isDualPlayer, Connectable con) {
        this.connectable=con;
        this.model = model;
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/ui/images/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFocusable(true);
        requestFocusInWindow();
        setupUIComponents();
        SetupTwoPlayerPanel();
        setupCallbacks();
    }

    private void setupUIComponents() {
        setLayout(new BorderLayout());

        TopMenuPanel topMenuPanel = new TopMenuPanel(model);
        topMenuPanel.addPauseButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPaused(true);
                model.setPaused(true);
            }
        });

        add(topMenuPanel, BorderLayout.NORTH);

        
    }

    void SetupTwoPlayerPanel(){
        gameStatusPanel = new GameStatusPanel();
        connectable.setGSP(gameStatusPanel);
        connectable.setModel(model);
        add(gameStatusPanel, BorderLayout.SOUTH);

        // Initialize countdown label
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 48));
        countdownLabel.setForeground(Color.WHITE);
        // Add countdown label to a layered pane to ensure visibility
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setBounds(0, 0, WIDTH, HEIGHT);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        countdownLabel.setBounds(0, 0, WIDTH, HEIGHT);
        layeredPane.add(countdownLabel, JLayeredPane.PALETTE_LAYER);
        add(countdownLabel, BorderLayout.CENTER);

    }

    public void updateChances() {
        ((TopMenuPanel) getComponent(0)).updateChances(model.getChances());
    }

    public void updateScore() {
        ((TopMenuPanel) getComponent(0)).updateScore(model.getScore());
    }

    // Additional methods to update GameStatusPanel
    public void updateGameStatusPanel() {
        if(connectable!=null){
            connectable.sendScore(model.getScore());
            connectable.sendLives(model.getChances());
            connectable.sendBarriersLeft(model.getBarriersLeft());
        }
    }

    private void setupCallbacks() {
        model.setScoreChangeCallback(new Runnable() {
            @Override
            public void run() {
                updateScore();
                updateGameStatusPanel(); // Update GameStatusPanel
            }
        });
    }

    private void setupYmirComponents() {
        ymirModel = new YmirModel();
        ymirView = new YmirView();
        ymirController = new YmirController(ymirModel, ymirView);

        ymirView.setBounds(WIDTH - 127, HEIGHT - 90, 300, 200);
        add(ymirView);

        ymirModel.setSpell(ObjectFactory.getInstance().createSpellIcons3(model));

        ymirController.start();
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
            pausePanel.setBounds(0, 0, DEFAULT.screenWidth, DEFAULT.screenWidth);
            pausePanel.setOpaque(false);

            JLabel pauseLabel = new JLabel("Game is paused");
            pauseLabel.setFont(new Font("Arial", Font.BOLD, 22));
            pauseLabel.setForeground(Color.WHITE);

            saveButton = new JButton("Save");
            saveButton.addActionListener(e -> model.saveGame(model.getGrid(), model.getGameId()));

            resumeButton = new JButton("Resume");
            resumeButton.addActionListener(e -> setPaused(false));

            quitButton = new JButton("Quit");
            quitButton.addActionListener(e -> quitGame());

            backToBuildingModeButton = new JButton("Turn Back To Building Mode");
            backToBuildingModeButton.addActionListener(e -> quitGame());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            pausePanel.add(pauseLabel, gbc);
            pausePanel.add(resumeButton, gbc);
            pausePanel.add(quitButton, gbc);
            pausePanel.add(saveButton, gbc);
            pausePanel.add(backToBuildingModeButton, gbc);
            add(pausePanel, BorderLayout.CENTER);
        }
    }

    public void quitGame() {
        BuildingModeModel model2 = new BuildingModeModel(model.getUser());
        BuildingModeView view2 = new BuildingModeView(model2);
        BuildingModeController controller2 = new BuildingModeController(model2, view2);

        JFrame newFrame = new JFrame();
        newFrame.add(view2);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.pack();
        newFrame.setVisible(true);

        //frame.setSize(SignInPage.WIDTH, SignInPage.HEIGHT);
        newFrame.setLocationRelativeTo(null);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.dispose();} // Close the current game window
            // Optionally, switch back to another view like the main menu
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
        if(this.connectable!=null){updateGameStatusPanel();}
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

            for (Box i : RunningModeModel.boxes) {
                if (i != null) {
                    i.draw(g);
                }
            }
            
            for (HollowPurpleBarrier b :model.getPurpleList()) {
                b.draw(g);
            }

            for (int j = 0; j < RunningModeModel.barriers.size(); j++) {
                Barrier i = RunningModeModel.barriers.get(j);
                if (i != null) {
                    i.draw(g);
                }
            }
            
            if (!model.getDebrisList().isEmpty()) {
                for (Debris d: model.getDebrisList()) {
                    d.draw(g);
                }
            }

            for (int i = 0; i < RunningModeModel.spells.size(); i++) {
                SpellIcon SI = RunningModeModel.spells.get(i);
                SI.setBounds(10, HEIGHT - 120 - i * 65, 50, 50);
                add(SI);
            }
        }
    }

    public void setCountdownText(String text) {
        countdownLabel.setText(text);
        revalidate();
        repaint();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();  
    }

    

    

}
