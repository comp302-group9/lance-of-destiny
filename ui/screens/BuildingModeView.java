package ui.screens;

import javax.swing.*;
import domain.models.RunningModeModel;
import domain.controllers.RunningModeController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import domain.models.BuildingModeModel;
import domain.objects.Barrier.Barrier;
import ui.screens.BModeUI.BarrierButton;
import ui.screens.BModeUI.BarrierElement;

public class BuildingModeView extends JPanel {
	public static final int HEIGHT = 600;
	public static final int WIDTH = HEIGHT * 16/9;
	private BufferedImage backgroundImage;
	private BuildingModeModel model;
    private JButton playButton;
	public int[][] grid;
	//int buttonWidth = 21 * WIDTH / 256;
	int buttonWidth = RunningModeModel.barrierWidth;
	int buttonHeight = RunningModeModel.barrierHeight;
	private JLabel simpleLabel;
	private JLabel reinforcedLabel;
	private JLabel explosiveLabel;
	private JLabel rewardingLabel;
	private BarrierElement[] elements = new BarrierElement[4];
	

	public static BarrierButton[] buttons = new BarrierButton[BuildingModeModel.ROWS * BuildingModeModel.COLUMNS];
	
	private ImageIcon empty = scaleImage("/ui/images/Empty3.png");
	private ImageIcon simple = scaleImage("/ui/images/simpleBarrierIcon.png");
	private ImageIcon firm = scaleImage("/ui/images/reinforcedBarrierIcon.png");
	private ImageIcon explosive = scaleImage("/ui/images/explosiveBarrierIcon.png");
	private ImageIcon rewarding = scaleImage("/ui/images/rewardingBarrierIcon.png");
	
	
	public BuildingModeView(BuildingModeModel model) {
		this.model = model;
		initializeUI();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(null);
		setBackground(Color.BLACK);

		try {
			java.net.URL imageURL = getClass().getResource("/ui/images/Background.png");
			backgroundImage = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//grid = model.readTxt("/domain/txtData/Test.txt");
		grid = model.createEmptyGrid();

		addEmptyButtons();
		readGrid(grid);
		currentState();
		addButton();
		addInputFields();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw the background image
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}

    protected void readGrid(int[][] grid){
        for (int i=0; i<BuildingModeModel.ROWS ;i++){
            for (int j=0; j<BuildingModeModel.COLUMNS ;j++){
                switch (grid[i][j]) {
					case 0:
                        buttons[BuildingModeModel.COLUMNS*i+j].setIcon(empty);
                        break;
                    case 1:
                        model.number_simple++;
                        buttons[BuildingModeModel.COLUMNS*i+j].setIcon(simple);
                        break;
                    case 2:
                        model.number_reinforced++;
                        buttons[BuildingModeModel.COLUMNS*i+j].setIcon(firm);
                        break;
                    case 3:
                        model.number_explosive++;
                        buttons[BuildingModeModel.COLUMNS*i+j].setIcon(explosive);
                        break;
                    case 4:
                        model.number_rewarding++;
                        buttons[BuildingModeModel.COLUMNS*i+j].setIcon(rewarding);
                        break;
                    }
                
            }
        }
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

	public void showErrorDialog() {
    // Show error dialog
    JOptionPane.showMessageDialog(this, "You gived wrong barrier numbers! Check for the barrier numbers! (Before try to play again, first place the barriers.)", "Barrier Number Validation Error", JOptionPane.ERROR_MESSAGE);
    
    // Create a timer to close the dialog after 5 seconds
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

	private void switchToRunningMode() {
		// Validate barriers before switching to the running mode
		if (!model.validateBarriers()) {
			showErrorDialog();
			return;
		}
		
		// Create the running mode components and switch views
		RunningModeModel model = new RunningModeModel();
		RunningModeView view = new RunningModeView(model);
		RunningModeController controller = new RunningModeController(model, view, grid);
	
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		frame.getContentPane().removeAll();
		frame.getContentPane().add(view);
		frame.revalidate();
		frame.repaint();
	
		Thread gameThread = new Thread(controller);
		gameThread.start();
	}	

	public void addEmptyButtons() {
		int xStart = HEIGHT / 32;
		int yStart = WIDTH / 32;
		int xGap = HEIGHT / 128;
		int yGap = WIDTH / 96;
		for (int row = 0; row < BuildingModeModel.ROWS; row++) {
			for (int col = 0; col < BuildingModeModel.COLUMNS; col++) {
				BarrierButton button = new BarrierButton(row,col);
				button.setFocusable(false);
				int x = xStart + col * (buttonWidth + xGap);
				int y = yStart + row * (buttonHeight + yGap);
				//if (row % 2 == 0) {x += WIDTH / 128;}
				button.setBounds(x, y, buttonWidth, buttonHeight);
				button.setContentAreaFilled(false);
				button.setBorderPainted(false);
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchBarrier(button);
						updateCurrent();
					}
				});
				button.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						button.setContentAreaFilled(true);
						button.setBackground(Color.white);
					}

					@Override
					public void mouseExited(MouseEvent e) {
						button.setContentAreaFilled(false);
					}
				});
				add(button);
				buttons[BuildingModeModel.COLUMNS * row + col] = button;
			}
		}
	}

	protected void currentState() {
		simpleLabel = new JLabel("Simple: " + model.number_simple);
		reinforcedLabel = new JLabel("Reinforced: " + model.number_reinforced);
		explosiveLabel = new JLabel("Explosive: " + model.number_explosive);
		rewardingLabel = new JLabel("Rewarding: " + model.number_rewarding);

        simpleLabel.setBounds(3*WIDTH/4, 12*HEIGHT/20, WIDTH/5, HEIGHT/20);
        reinforcedLabel.setBounds(3*WIDTH/4, 12*HEIGHT/20+30, WIDTH/5, HEIGHT/20);
        explosiveLabel.setBounds(3*WIDTH/4, 12*HEIGHT/20+60, WIDTH/5, HEIGHT/20);
        rewardingLabel.setBounds(3*WIDTH/4, 12*HEIGHT/20+90, WIDTH/5, HEIGHT/20);

        Font labelFont = new Font("Old English Text MT", Font.ITALIC, WIDTH/35);
        Color labelColor = Color.WHITE;

		simpleLabel.setFont(labelFont);
		simpleLabel.setForeground(labelColor);
		reinforcedLabel.setFont(labelFont);
		reinforcedLabel.setForeground(labelColor);
		explosiveLabel.setFont(labelFont);
		explosiveLabel.setForeground(labelColor);
		rewardingLabel.setFont(labelFont);
		rewardingLabel.setForeground(labelColor);

		this.add(simpleLabel);
		this.add(reinforcedLabel);
		this.add(explosiveLabel);
		this.add(rewardingLabel);
	}

	public void updateCurrent() {
		simpleLabel.setText("Simple: " + model.number_simple);
		reinforcedLabel.setText("Reinforced: " + model.number_reinforced);
		explosiveLabel.setText("Explosive: " + model.number_explosive);
		rewardingLabel.setText("Rewarding: " + model.number_rewarding);
	}

	public void resetCurrent(){
		model.setNumber_simple(0);
		model.setNumber_reinforced(0);
		model.setNumber_explosive(0);
		model.setNumber_rewarding(0);
	}

	public void switchBarrier(BarrierButton button) {
		int row = button.getRow();
		int col = button.getCol();
	
		if (button.getIcon() == empty) {
			button.setIcon(simple);
			model.setNumber_simple(model.getNumber_simple() + 1);
			grid[row][col] = 1;
		} else if (button.getIcon() == simple) {
			button.setIcon(firm);
			model.setNumber_simple(model.getNumber_simple() - 1);
			model.setNumber_reinforced(model.getNumber_reinforced() + 1);
			grid[row][col] = 2;
		} else if (button.getIcon() == firm) {
			button.setIcon(explosive);
			model.setNumber_reinforced(model.getNumber_reinforced() - 1);
			model.setNumber_explosive(model.getNumber_explosive() + 1);
			grid[row][col] = 3;
		} else if (button.getIcon() == explosive) {
			button.setIcon(rewarding);
			model.setNumber_explosive(model.getNumber_explosive() - 1);
			model.setNumber_rewarding(model.getNumber_rewarding() + 1);
			grid[row][col] = 4;
		} else if (button.getIcon() == rewarding) {
			button.setIcon(empty);
			model.setNumber_rewarding(model.getNumber_rewarding() - 1);
			grid[row][col] = 0;
		}
	}

	public void addButton() {
		JButton switchPanelButton = new JButton("Play");
		switchPanelButton.setBounds(600, 490, 120, 30); // Adjust the position and size as needed
		switchPanelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RunningModeModel model = new RunningModeModel();
				RunningModeView view = new RunningModeView(model);
				RunningModeController controller = new RunningModeController(model, view, grid);

				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(BuildingModeView.this);

				frame.getContentPane().removeAll();
				frame.getContentPane().add(view);
				frame.revalidate();
				frame.repaint();

				Thread gameThread = new Thread(controller);
				gameThread.start();
			}
		});
		add(switchPanelButton);
		
		
		JButton placeButton = new JButton("Place");
		placeButton.setBounds(600, 525, 120, 30);
		placeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetCurrent();
				int[][] temp = model.createEmptyGrid();

				for (int i=0;i<4;i++){
					if(isValidInteger(elements[i].getTextFieldText())){
						int value = Integer.parseInt(elements[i].getTextFieldText());
						changeRandomValues(temp, value, i+1);
					}
				}

				grid = temp;
			    readGrid(grid);
				updateCurrent();
			}
		});
		add(placeButton);
		
		JButton saveButton = new JButton("Save");
		saveButton.setBounds(600, 560, 120, 30);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.writeTxt("domain\\txtData\\Test.txt", grid);
			}
		});
		
		add(saveButton);

		JButton loadButton = new JButton("Load");
		loadButton.setBounds(730, 560, 120, 30);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetCurrent();
				grid = model.readTxt("/domain/txtData/Test.txt");
				readGrid(grid);
				updateCurrent();

			}
		});
		add(loadButton);
	} 

	public static boolean isValidInteger(String input) {
        String regex = "^[1-9]\\d*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

	private void addInputFields() {

		ArrayList<Barrier> bList = model.getBarrierList();

		int yStart = 39 * HEIGHT / 64;
		int xStart = WIDTH / 15;
		int panelWidth = 200;
		int panelHeight = 100;
		int gap = HEIGHT / 30;

		for (int i = 0; i < 4; i++) {
    		BarrierElement barrierElement = new BarrierElement(bList.get(i));

    		int row = i / 2; // Assuming you want 2 panels per row
    		int col = i % 2; // Assuming you want 2 panels per row

    		int x = xStart + col * (panelWidth + gap);
    		int y = yStart + row * (panelHeight + gap);
			
    		barrierElement.setBounds(x, y, panelWidth, panelHeight);

    		add(barrierElement);
			elements[i]=barrierElement;
		}
	}
	
	public void changeRandomValues(int[][] array, int numChanges, int replacementValue) {
        int rows = array.length;
        int cols = array[0].length;
		int change_num = 0;

        Random random = new Random();
        while (change_num < numChanges) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);

            if (array[row][col] == 0) {
                array[row][col] = replacementValue;
				change_num++;
            }
        }
    }

	private void initializeUI() {
		JButton switchPanelButton = new JButton("Play");
		switchPanelButton.setBounds(600, 490, 120, 30); // Adjust the position and size as needed
		switchPanelButton.addActionListener(e -> switchToRunningMode());
		add(switchPanelButton);
	}

    private ActionListener createPlayButtonListener() {
        return e -> {
            // Create the running mode model, view, and controller
            RunningModeModel runningModel = new RunningModeModel();
            RunningModeView runningView = new RunningModeView(runningModel);
            RunningModeController runningController = new RunningModeController(runningModel, runningView, grid);

            // Get the parent frame of this panel to switch content
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(runningView);
            frame.revalidate();
            frame.repaint();

            // Start the game loop in a new thread
            Thread gameThread = new Thread(runningController);
            gameThread.start();
        };
    }
	
}