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

        Label title = new Label("Bank Gaborone");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        Label subtitle = new Label("Please enter your login credentials to continue");

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDefaultButton(true); // pressing Enter clicks Login

        messageLabel.setStyle("-fx-text-fill: #c0392b;"); // red text for errors

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
