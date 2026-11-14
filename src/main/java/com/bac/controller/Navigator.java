package com.bac.controller;

import com.bac.model.Bank;
import com.bac.view.LoginView;
import com.bac.view.MainMenuView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Switches between screens. Each screen is created together with its controller.
public class Navigator {

    private final Stage stage;
    private final Bank bank;

    public Navigator(Stage stage, Bank bank) {
        this.stage = stage;
        this.bank = bank;
    }

    public void showLogin() {
        LoginView view = new LoginView();
        new LoginController(view, this);
        show(view, 420, 460, "Login");
    }

    public void showMainMenu() {
        MainMenuView view = new MainMenuView();
        new MainMenuController(view, this, bank);
        show(view, 420, 480, "Main Menu");
    }

    private void show(Parent root, double width, double height, String title) {
        stage.setScene(new Scene(root, width, height));
        stage.setTitle(bank.getName() + " - " + title);
        stage.show();
    }
}
