package com.bac;

import com.bac.controller.Navigator;
import com.bac.model.Bank;
import javafx.application.Application;
import javafx.stage.Stage;

public class BankingApp extends Application {

    @Override
    public void start(Stage stage) {
        Bank bank = new Bank("Bank Gaborone", "Gaborone Main");
        Navigator navigator = new Navigator(stage, bank);
        navigator.showLogin();
    }
}