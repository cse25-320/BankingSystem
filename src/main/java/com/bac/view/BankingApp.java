package com.bac.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BankingApp extends Application {

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView();
        stage.setScene(new Scene(loginView, 420, 420));
        stage.setTitle("Bank Gaborone - Login");
        stage.show();
    }
}
