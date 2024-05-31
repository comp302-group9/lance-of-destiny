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
	
}