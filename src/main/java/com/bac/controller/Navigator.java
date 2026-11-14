package com.bac.controller;

import com.bac.view.AccountView;
import com.bac.view.OpenAccountView;
import com.bac.view.RegisterCustomerView;
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

    public void showRegisterCustomer() {
        RegisterCustomerView view = new RegisterCustomerView();
        new RegisterCustomerController(view, this, bank);
        show(view, 480, 420, "Register Customer");
    }

    public void showOpenAccount() {
        OpenAccountView view = new OpenAccountView();
        new OpenAccountController(view, this, bank);
        show(view, 540, 540, "Open Account");
    }

    public void showAccountView() {
        AccountView view = new AccountView();
        new AccountController(view, this, bank);
        show(view, 680, 680, "Accounts & Transactions");
    }
    private void show(Parent root, double width, double height, String title) {
        stage.setScene(new Scene(root, width, height));
        stage.setTitle(bank.getName() + " - " + title);
        stage.show();
    }
}
