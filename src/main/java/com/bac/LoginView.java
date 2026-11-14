package com.bac.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

// login screen and input reading
public class LoginView extends VBox {

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Login");
    private final Label messageLabel = new Label();

    public LoginView() {
        setSpacing(12);
        setPadding(new Insets(40));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72, #2a5298);");

        Label title = new Label("Bank Gaborone");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: white;");
        Label subtitle = new Label("Please enter your login credentials to continue");
        subtitle.setStyle("-fx-text-fill: #d6e4ff;");

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDefaultButton(true);
        loginButton.setStyle("-fx-background-color:#f5b400;-fx-text-fill:#1a1a1a;"+"-fx-font-weight:bold;-fx-background-radius:6;");

        messageLabel.setStyle("-fx-text-fill: #ffb4a9;");

        getChildren().addAll(title, subtitle, usernameField, passwordField, loginButton, messageLabel);
    }

    // controller reads...
    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    // controller tells screen to...
    public void setOnLoginAction(EventHandler<ActionEvent> handler) {
        loginButton.setOnAction(handler);
    }

    public void showError(String message) {
        messageLabel.setText(message);
    }

    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
}
