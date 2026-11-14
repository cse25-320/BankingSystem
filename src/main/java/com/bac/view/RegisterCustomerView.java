package com.bac.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// BOUNDARY CLASS: form for registering a customer. No business logic.
public class RegisterCustomerView extends VBox {

    private final TextField firstNameField = new TextField();
    private final TextField surnameField = new TextField();
    private final TextField addressField = new TextField();
    private final Button registerButton = new Button("Register");
    private final Button backButton = new Button("Back to Menu");
    private final Label messageLabel = new Label();

    public RegisterCustomerView() {
        setSpacing(18);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72, #2a5298);");

        Label title = new Label("Register Customer");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        String focusStyle = "-fx-focus-color: #f5b400; -fx-faint-focus-color: transparent;";
        firstNameField.setStyle(focusStyle);
        surnameField.setStyle(focusStyle);
        addressField.setStyle(focusStyle);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setAlignment(Pos.CENTER);
        form.add(whiteLabel("First name"), 0, 0);
        form.add(firstNameField, 1, 0);
        form.add(whiteLabel("Surname"), 0, 1);
        form.add(surnameField, 1, 1);
        form.add(whiteLabel("Address"), 0, 2);
        form.add(addressField, 1, 2);

        registerButton.setStyle("-fx-background-color: #f5b400; -fx-text-fill: #1a1a1a; "
                + "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 20;");
        backButton.setStyle("-fx-background-color: #d6e4ff; -fx-text-fill: #1a1a1a; "
                + "-fx-background-radius: 6; -fx-padding: 8 20;");
        registerButton.setDefaultButton(true);

        HBox buttons = new HBox(12, registerButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        getChildren().addAll(title, form, buttons, messageLabel);
    }

    private Label whiteLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;");
        return label;
    }

    // ----- what the controller can read from the form -----
    public String getFirstName() { return firstNameField.getText().trim(); }
    public String getSurname() { return surnameField.getText().trim(); }
    public String getAddress() { return addressField.getText().trim(); }

    // ----- what the controller can tell the screen -----
    public void setOnRegisterAction(EventHandler<ActionEvent> handler) {
        registerButton.setOnAction(handler);
    }

    public void setOnBackAction(EventHandler<ActionEvent> handler) {
        backButton.setOnAction(handler);
    }

    public void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: #a8f0b8; -fx-font-weight: bold;");
        messageLabel.setText(message);
    }

    public void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: #ffb4a9; -fx-font-weight: bold;");
        messageLabel.setText(message);
    }

    public void clearFields() {
        firstNameField.clear();
        surnameField.clear();
        addressField.clear();
    }
}
