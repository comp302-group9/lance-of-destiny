package domain.controllers;

import domain.models.User;
import ui.screens.SignInView;
import ui.screens.SignUpView;

public class SignUpController {

    private User model;
    private SignUpView signUpView;

    public SignUpController(SignUpView view) {
        this.signUpView = view;

        setupListeners();
    }

    private void setupListeners() {
        signUpView.addSignUpButtonListener(e -> register());
        signUpView.addBackButtonListener(e -> showSignInView());
    }

    private void register() {
        String username = signUpView.getUsername();
        String email = signUpView.getEmail(); // Assuming you want to store the email too
        String password = signUpView.getPassword();
        String confirmPassword = signUpView.getConfirmPassword();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signUpView.setStatus("Please fill all fields!");
        } else if (!password.equals(confirmPassword)) {
            signUpView.setStatus("Passwords do not match!");
        } else {
            model.setUsername(username);
            model.setPassword(password); // Update model, ideally should use more secure methods
            signUpView.setStatus("Registration successful!");
        }
    }

    private void showSignInView() {
        SignInView signInView = new SignInView();
        SignInController controller = new SignInController(signInView);
        signInView.display();
    }
}
