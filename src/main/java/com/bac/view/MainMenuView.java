package com.bac.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

// BOUNDARY CLASS
public class MainMenuView extends VBox {

    private final Label welcomeLabel = new Label();
    private final Button registerCustomerButton = new Button("Register Customer");
    private final Button openAccountButton = new Button("Open Account");
    private final Button viewAccountsButton = new Button("View Accounts & Transactions");
    private final Button payInterestButton = new Button("Pay Monthly Interest");
    private final Button logoutButton = new Button("Logout");

    public MainMenuView() {
        setSpacing(12);
        setPadding(new Insets(40));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72, #2a5298);");

        Label title = new Label("Main Menu");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: white;");

        makeWide(registerCustomerButton);
        makeWide(openAccountButton);
        makeWide(viewAccountsButton);
        makeWide(payInterestButton);
        makeWide(logoutButton);

        getChildren().addAll(title, welcomeLabel, registerCustomerButton, openAccountButton,
                viewAccountsButton, payInterestButton, logoutButton);
    }

    private void styleButton(Button button, String backgroundColour, String textColour) {
        button.setMaxWidth(Double.MAX_VALUE);
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-background-color: " + backgroundColour + "; "
                + "-fx-text-fill: " + textColour + "; "
                + "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 10 16;");
    }
    private void makeWide(Button button) {
        button.setMaxWidth(Double.MAX_VALUE);
    }

    public void setWelcomeText(String text) {
        welcomeLabel.setText(text);
    }

    public void setOnRegisterCustomerAction(EventHandler<ActionEvent> handler) {
        registerCustomerButton.setOnAction(handler);
    }

    public void setOnOpenAccountAction(EventHandler<ActionEvent> handler) {
        openAccountButton.setOnAction(handler);
    }

    public void setOnViewAccountsAction(EventHandler<ActionEvent> handler) {
        viewAccountsButton.setOnAction(handler);
    }

    public void setOnPayInterestAction(EventHandler<ActionEvent> handler) {
        payInterestButton.setOnAction(handler);
    }

    public void setOnLogoutAction(EventHandler<ActionEvent> handler) {
        logoutButton.setOnAction(handler);
    }
}
