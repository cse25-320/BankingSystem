package com.bac.controller;

import com.bac.view.LoginView;

public class LoginController {

    private static final String TELLER_USERNAME = "Bame";
    private static final String TELLER_PASSWORD = "0306";

    private final LoginView view;
    private final Navigator navigator;

    public LoginController(LoginView view, Navigator navigator) {
        this.view = view;
        this.navigator = navigator;
        view.setOnLoginAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.showError("Please enter your username and password");
            return;
        }

        if (username.equals(TELLER_USERNAME) && password.equals(TELLER_PASSWORD)) {
            navigator.showMainMenu();
        } else {
            view.showError("Invalid username or password");
        }
    }
}