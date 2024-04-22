package domain.controllers;

import domain.models.User;
import ui.screens.SignInView;
import ui.screens.SignUpView;

public class SignInController {

    private User user;
    private SignInView signInView;

    public SignInController(SignInView view) {
        this.signInView = view;

        setupListeners();
    }

    private void setupListeners() {
        signInView.addSignInButtonListener(e -> signIn());
        signInView.addSignUpButtonListener(e -> showSignUpView());
    }

    private void signIn() {
        String username = signInView.getUsername();
        String password = signInView.getPassword();

        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            signInView.setSignInStatus("Sign in successful.");
        } else {
            signInView.setSignInStatus("Invalid credentials.");
        }
    }

    private void showSignUpView() { 
        SignUpView signUpView = new SignUpView();
        SignUpController controller = new SignUpController(signUpView);
        //signUpView.setVisible(false);
        //controller.getSignUpView().setVisible(true);
        //controller.getSignInView().setVisible(false);
    }
}
