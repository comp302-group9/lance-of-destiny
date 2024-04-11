package ui.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domain.controllers.RunningModeController;
import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import domain.objects.Barrier.Barrier;
import ui.screens.BModeUI.BarrierElement;

public class BuildingModeView extends JPanel {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private BufferedImage backgroundImage;
	private BuildingModeModel model;
	public int[][] grid;
	int buttonWidth = 7 * HEIGHT / 64;
	int buttonHeight = 2 * WIDTH / 72;
	private ImageIcon empty = scaleImage("/ui/images/Empty3.png");
	private ImageIcon simple = scaleImage("/ui/images/simpleBarrierIcon.png");
	private ImageIcon firm = scaleImage("/ui/images/reinforcedBarrierIcon.png");
	private ImageIcon explosive = scaleImage("/ui/images/explosiveBarrierIcon.png");
	private ImageIcon rewarding = scaleImage("/ui/images/rewardingBarrierIcon.png");
	private JLabel simpleLabel;
	private JLabel reinforcedLabel;
	private JLabel explosiveLabel;
	private JLabel rewardingLabel;
	private BarrierElement[] elements = new BarrierElement[4];
	
	public static final int ROWS = 10;
	public static final int COLUMNS = 11;
	public static JButton[] buttons = new JButton[10* BuildingModeModel.ROWS + BuildingModeModel.COLUMNS];

	public BuildingModeView(BuildingModeModel model) {
		this.model = model;
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
                if(grid[i][j]!=0){
                switch (grid[i][j]) {
                    case 1:
                        model.number_simple++;
                        buttons[BuildingModeModel.ROWS*i+j].setIcon(simple);
                        break;
                    case 2:
                        model.number_reinforced++;
                        buttons[BuildingModeModel.ROWS*i+j].setIcon(firm);
                        break;
                    case 3:
                        model.number_explosive++;
                        buttons[BuildingModeModel.ROWS*i+j].setIcon(explosive);
                        break;
                    case 4:
                        model.number_rewarding++;
                        buttons[BuildingModeModel.ROWS*i+j].setIcon(rewarding);
                        break;
                    }
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

	public void addEmptyButtons() {
		int xStart = HEIGHT / 32;
		int yStart = WIDTH / 32;
		int xGap = HEIGHT / 128;
		int yGap = WIDTH / 96;
		for (int row = 0; row < BuildingModeModel.ROWS; row++) {
			for (int col = 0; col < BuildingModeModel.COLUMNS; col++) {
				JButton button = new JButton(empty);
				button.setFocusable(false);
				int x = xStart + col * (buttonWidth + xGap);
				int y = yStart + row * (buttonHeight + yGap);
				if (row % 2 == 0) {
					x += WIDTH / 128;
				}
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
				buttons[BuildingModeModel.ROWS * row + col] = button;
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

	public void switchBarrier(JButton button) {
		if (button.getIcon() == empty) {
			button.setIcon(simple);
			model.number_simple++;
		} else if (button.getIcon() == simple) {
			button.setIcon(firm);
			model.number_simple--;
			model.number_reinforced++;
		} else if (button.getIcon() == firm) {
			button.setIcon(explosive);
			model.number_reinforced--;
			model.number_explosive++;
		} else if (button.getIcon() == explosive) {
			button.setIcon(rewarding);
			model.number_explosive--;
			model.number_rewarding++;
		} else if (button.getIcon() == rewarding) {
			button.setIcon(empty);
			model.number_rewarding--;
		}
	}

	public void addButton() {
		JButton switchPanelButton = new JButton("Switch Panel");
		switchPanelButton.setBounds(20, 380, 120, 30); // Adjust the position and size as needed
		switchPanelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RunningModeModel model = new RunningModeModel();
				RunningModeView view = new RunningModeView(model);
				RunningModeController controller = new RunningModeController(model, view);

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
		placeButton.setBounds(20, 400, 120, 30);
		placeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt(elements[0].getTextFieldText());
			    model.setNumber_simple(value);
			    
			    int value1 = Integer.parseInt(elements[1].getTextFieldText());
			    model.setNumber_simple(value1);
			    
			    int value2 = Integer.parseInt(elements[2].getTextFieldText());
			    model.setNumber_simple(value2);
			    
			    int value3 = Integer.parseInt(elements[3].getTextFieldText());
			    model.setNumber_simple(value3);
			    
			    System.out.println(value);
			    System.out.println(value1);
			    System.out.println(value2);
			    System.out.println(value3);
			    
			}
		});
		add(placeButton);
	}

	private void addInputFields() {

		ArrayList<Barrier> bList = model.getBarrierList();

		int yStart = 39 * HEIGHT / 64;
		int xStart = WIDTH / 30;
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
}
