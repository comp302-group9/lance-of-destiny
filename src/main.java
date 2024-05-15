// Main.java
import javax.swing.SwingUtilities;

import domain.controllers.SignInController;
import ui.screens.SignInView;

public class main { 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
        	
        	public void run() {
                showSignInView();
        	} 
            
        });
    }
    
    public static void showSignInView() {
        SignInView signInView = new SignInView();
        SignInController controller = new SignInController(signInView); 
    }
	
	/*
	private static JFrame frame;
    private static User model;
	
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(new Runnable() {
        	
        	public void run() {
        		frame = new JFrame("MVC Application");
                model = new User("admin", "password123"); // Default credentials
                showSignInView();
        	}
            
        });
    }
    
    public static void showSignInView() {
        SignInView signInView = new SignInView();
        new SignInController(model, signInView, frame);
        //frame.setPreferredSize(new Dimension(900, 600));
        frame.setContentPane(signInView.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        */

    }
    // Create the model, view, and controller
    
    
	//User model = new User("admin", "password123"); // Default credentials
    //SignInView view = new SignInView();
    //SignInController controller = new SignInController(model, view);
    
    
    //Create the main frame
    //JFrame frame = new JFrame("Building Mode");
    //frame.getContentPane().add(view);
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //frame.pack();
    //frame.setVisible(true);
    //frame.setSize(SignInPage.WIDTH, SignInPage.HEIGHT);
    //frame.setLocationRelativeTo(null); // Center the frame
    //frame.setResizable(false);

    // Start the game loop
    

