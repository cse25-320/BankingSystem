package com.bac.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BankingApp extends Application {

    @Override
    public void start(Stage stage) {
        OpenAccountView view = new OpenAccountView();
        stage.setScene(new Scene(view, 520, 520));
        stage.setTitle("Bank Gaborone");
        stage.show();
    }
}
