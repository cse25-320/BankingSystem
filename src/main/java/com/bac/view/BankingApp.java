package com.bac.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BankingApp extends Application {

    @Override
    public void start(Stage stage) {
        AccountView view = new AccountView();
        stage.setScene(new Scene(view, 640, 640));
        stage.setTitle("Bank Gaborone");
        stage.show();
    }
}
