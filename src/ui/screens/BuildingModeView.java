package ui.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import domain.DEFAULT;
import domain.controllers.MyMouseListener;
import domain.controllers.RunningModeController;
import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import domain.objects.Barrier.Barrier;
import network.ClientController;
import network.ClientModel;
import network.ClientView;
import network.ServerController;
import network.ServerModel;
import network.ServerView;
import ui.screens.BModeUI.BarrierButton;
import ui.screens.BModeUI.BarrierElement;
import ui.screens.BModeUI.BarrierStates.EmptyState;
import ui.screens.BModeUI.BarrierStates.ExplosiveState;
import ui.screens.BModeUI.BarrierStates.ReinforcedState;
import ui.screens.BModeUI.BarrierStates.RewardingState;
import ui.screens.BModeUI.BarrierStates.SimpleState;

public class BuildingModeView extends JPanel {
	public int WIDTH=DEFAULT.screenWidth;
	private int HEIGHT=DEFAULT.screenHeight;
	private BufferedImage backgroundImage;
	private BuildingModeModel model;
    private JButton playButton, placeButton, saveButton, myGamesButton, helpButton, hostPanelButton, clientPanelButton;
	public int[][] grid;
	int buttonWidth = RunningModeModel.barrierWidth;
	int buttonHeight = RunningModeModel.barrierHeight;
    private JLabel simpleLabel, reinforcedLabel, explosiveLabel, rewardingLabel;
	private BarrierElement[] elements = new BarrierElement[4];
	public static BarrierButton[] buttons = new BarrierButton[DEFAULT.ROWS * DEFAULT.COLUMNS];
    private ImageIcon empty, simple, firm, explosive, rewarding;
	
	
	public BuildingModeView(BuildingModeModel model) {
        this.model = model;
        initializeComponents();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        setBackground(Color.BLACK);
        loadImage();
        grid = model.createEmptyGrid();
        addComponents();
    }

    private void loadImage() {
        try {
            java.net.URL imageURL = getClass().getResource("/ui/images/Background.png");
            backgroundImage = ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}

	private void initializeComponents() {
        initializeButtons();
        initializeLabels();
        initializeIcons();
    }

	private void initializeButtons() {
        playButton = createButton("Play", 600, 490);
        placeButton = createButton("Place", 600, 525);
        saveButton = createButton("Save", 600, 560);
        myGamesButton = createButton("My Games", 730, 560);

        helpButton = createButton("? Help Menu ?", WIDTH - 200, 5);
        helpButton.addActionListener(e -> showHelpMenu());

        hostPanelButton = createButton("Host", 730, 490);
        hostPanelButton.addActionListener(e -> hostMenu());

        clientPanelButton = createButton("Client", 730, 525);
        clientPanelButton.addActionListener(e -> clientPage());
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 120, 30);
        add(button);
        return button;
	}

	private void initializeLabels() {
        simpleLabel = createLabel("Simple: 0", 3 * WIDTH / 4, 12 * HEIGHT / 20);
        reinforcedLabel = createLabel("Reinforced: 0", 3 * WIDTH / 4, 12 * HEIGHT / 20 + 30);
        explosiveLabel = createLabel("Explosive: 0", 3 * WIDTH / 4, 12 * HEIGHT / 20 + 60);
        rewardingLabel = createLabel("Rewarding: 0", 3 * WIDTH / 4, 12 * HEIGHT / 20 + 90);
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, WIDTH / 5, HEIGHT / 20);
        label.setFont(new Font("Old English Text MT", Font.ITALIC, WIDTH / 35));
        label.setForeground(Color.WHITE);
        add(label);
        return label;
    }

	private void initializeIcons() {
        empty = scaleImage("/ui/images/Empty3.png");
        simple = scaleImage("/ui/images/simpleBarrierIcon.png");
        firm = scaleImage("/ui/images/reinforcedBarrierIcon.png");
        explosive = scaleImage("/ui/images/explosiveBarrierIcon.png");
        rewarding = scaleImage("/ui/images/rewardingBarrierIcon.png");
    }

	private ImageIcon scaleImage(String imagePath) {
		try {
			// Load the image
			BufferedImage image = ImageIO.read(getClass().getResource(imagePath));

			// Scale the image to fit the button
			Image scaledImage = image.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImage);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private void addComponents() {
        addEmptyButtons();
        readGrid(grid);
        updateCurrentState();
        addInputFields();
    }

	/**
	 * addEmptyButtons Method:
	 * 
	 * Initializes and adds empty barrier buttons to the view in a grid layout.
	 * 
	 * Requires:
	 * - BuildingModeModel.ROWS and BuildingModeModel.COLUMNS must be properly initialized.
	 * - buttonWidth and buttonHeight must be set to valid dimensions.
	 * - This instance of BuildingModeView must be properly initialized.
	 * 
	 * Modifies:
	 * - This instance of BuildingModeView by adding BarrierButton components.
	 * - The buttons array by storing references to the created BarrierButton instances.
	 * 
	 * Effects:
	 * - Adds a grid of BarrierButton instances to the panel based on the number of rows and columns.
	 * - Sets button properties: non-focusable, specific bounds, no content area filled, and no border painted.
	 * - Attaches an ActionListener to each button that calls switchBarrier(button) and updateCurrent() on action events.
	 * - Attaches a MyMouseListener to each button.
	 * - Updates the buttons array with the created buttons.
	 */
	private void addEmptyButtons() {
        int xStart = HEIGHT / 32;
        int yStart = WIDTH / 32;
        int xGap = HEIGHT / 128;
        int yGap = WIDTH / 96;
    
        for (int row = 0; row < BuildingModeModel.ROWS; row++) {
            for (int col = 0; col < BuildingModeModel.COLUMNS; col++) {
                BarrierButton button = new BarrierButton(row, col);
                button.setFocusable(false);
                button.setBounds(xStart + col * (RunningModeModel.barrierWidth + xGap), yStart + row * (RunningModeModel.barrierHeight + yGap), RunningModeModel.barrierWidth, RunningModeModel.barrierHeight);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setIcons(empty, simple, firm, explosive, rewarding);
                button.addActionListener(e -> {
                    button.switchBarrier(model, grid);
                    updateCurrentState();
                });
                button.addMouseListener(new MyMouseListener());
                add(button);
                buttons[BuildingModeModel.COLUMNS * row + col] = button;
            }
        }
    }
    

	private void showHelpMenu() {
        HelpMenu helpMenu = new HelpMenu(this);
        helpMenu.setVisible(true);
    }

    public void showErrorDialog() {
        JOptionPane.showMessageDialog(this, "You gave wrong barrier numbers! Check the barrier numbers! (Before trying to play again, first place the barriers.)", "Barrier Number Validation Error", JOptionPane.ERROR_MESSAGE);

        Timer closeDialogTimer = new Timer(5000, e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            if (win instanceof JDialog) {
                JDialog dialog = (JDialog) win;
                dialog.dispose();
            }
        });
        closeDialogTimer.setRepeats(false);
        closeDialogTimer.start();
    }
	
    public void readGrid(int[][] grid) {
    resetCurrentState(); // Reset current state before reading the grid
    for (int i = 0; i < DEFAULT.ROWS; i++) {
        for (int j = 0; j < DEFAULT.COLUMNS; j++) {
            BarrierButton button = buttons[DEFAULT.COLUMNS * i + j];
            switch (grid[i][j]) {
                case 0:
                    button.setState(new EmptyState());
                    button.setIcon(button.getEmptyIcon());
                    break;
                case 1:
                    model.setNumber_simple(model.getNumber_simple() + 1);
                    button.setState(new SimpleState());
                    button.setIcon(button.getSimpleIcon());
                    break;
                case 2:
                    model.setNumber_reinforced(model.getNumber_reinforced() + 1);
                    button.setState(new ReinforcedState());
                    button.setIcon(button.getFirmIcon());
                    break;
                case 3:
                    model.setNumber_explosive(model.getNumber_explosive() + 1);
                    button.setState(new ExplosiveState());
                    button.setIcon(button.getExplosiveIcon());
                    break;
                case 4:
                    model.setNumber_rewarding(model.getNumber_rewarding() + 1);
                    button.setState(new RewardingState());
                    button.setIcon(button.getRewardingIcon());
                    break;
            }
        }
    }
    updateCurrentState(); // Update the current state labels after reading the grid
}


	public void updateCurrentState() {
        simpleLabel.setText("Simple: " + model.number_simple);
        reinforcedLabel.setText("Reinforced: " + model.number_reinforced);
        explosiveLabel.setText("Explosive: " + model.number_explosive);
        rewardingLabel.setText("Rewarding: " + model.number_rewarding);
    }

	public void resetCurrentState() {
        model.setNumber_simple(0);
        model.setNumber_reinforced(0);
        model.setNumber_explosive(0);
        model.setNumber_rewarding(0);
    }

	private void switchBarrier(BarrierButton button) {
        button.switchBarrier(model, grid);
    }    

	public void addInputFields() {
        ArrayList<Barrier> barrierList = model.getBarrierList();
        int yStart = 39 * HEIGHT / 64;
        int xStart = WIDTH / 15;
        int panelWidth = 200;
        int panelHeight = 100;
        int gap = HEIGHT / 30;

        for (int i = 0; i < 4; i++) {
            BarrierElement barrierElement = new BarrierElement(barrierList.get(i));
            int row = i / 2;
            int col = i % 2;
            int x = xStart + col * (panelWidth + gap);
            int y = yStart + row * (panelHeight + gap);
            barrierElement.setBounds(x, y, panelWidth, panelHeight);
            add(barrierElement);
            elements[i] = barrierElement;
        }
    }

    private void hostMenu() {
            ServerModel model = new ServerModel();
            ServerView view = new ServerView(this.model.getUser().getUsername());
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(view);
            frame.revalidate();
            frame.repaint();

            ServerController.getInstance(model, view, this.model, this);
        }

        private void clientPage(){

            String serverAddress = JOptionPane.showInputDialog("Enter server IP address:");
            String clientName = model.getUser().getUsername();
    
            ClientModel model = new ClientModel(serverAddress, clientName);
            ClientView view = new ClientView(clientName);
    
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(view);
            frame.revalidate();
            frame.repaint();
            
            ClientController.getInstance(model, view, this.model, this);
        }

	public static boolean isValidInteger(String input) {
        String regex = "^[1-9]\\d*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

	public void changeRandomValues(int[][] array, int numChanges, int replacementValue) {
        int rows = array.length;
        int cols = array[0].length;
        int changeNum = 0;
        Random random = new Random();

        while (changeNum < numChanges) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (array[row][col] == 0) {
                array[row][col] = replacementValue;
                changeNum++;
            }
        }
    }

	public void addPlayButtonListener(ActionListener listener) {playButton.addActionListener(listener);}
    public void addPlaceButtonListener(ActionListener listener) {placeButton.addActionListener(listener);}
    public void addSaveButtonListener(ActionListener listener) {saveButton.addActionListener(listener);}
    public void addMyGamesButtonListener(ActionListener listener) {myGamesButton.addActionListener(listener);}

    public String getBarrierElementText(int index) {
        return elements[index].getTextFieldText();
    }

    public int[][] getGrid() {return grid;}
    public void setGrid(int[][] grid) {this.grid = grid;}
}