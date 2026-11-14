package com.bac.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// BOUNDARY CLASS: form for opening an account. No business logic.
public class OpenAccountView extends VBox {

    private final TextField customerIdField = new TextField();
    private final ComboBox<String> accountTypeBox = new ComboBox<>();
    private final TextField depositField = new TextField();
    private final TextField employerNameField = new TextField();
    private final TextField employerAddressField = new TextField();
    private final Label employerNameLabel = whiteLabel("Employer name");
    private final Label employerAddressLabel = whiteLabel("Employer address");
    private final Button openButton = new Button("Open Account");
    private final Button backButton = new Button("Back to Menu");
    private final Label messageLabel = new Label();

    public OpenAccountView() {
        setSpacing(18);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72, #2a5298);");

        Label title = new Label("Open Account");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        String focusStyle = "-fx-focus-color: #f5b400; -fx-faint-focus-color: transparent;";
        customerIdField.setStyle(focusStyle);
        accountTypeBox.setStyle(focusStyle);
        depositField.setStyle(focusStyle);
        employerNameField.setStyle(focusStyle);
        employerAddressField.setStyle(focusStyle);

        accountTypeBox.getItems().addAll("Savings", "Investment", "Cheque");
        accountTypeBox.setPromptText("Select account type");
        accountTypeBox.setMaxWidth(Double.MAX_VALUE);

        // Employer fields only matter for Cheque accounts, so show them only then
        setEmployerFieldsVisible(false);
        accountTypeBox.setOnAction(e ->
                setEmployerFieldsVisible("Cheque".equals(accountTypeBox.getValue())));

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setAlignment(Pos.CENTER);
        form.add(whiteLabel("Customer ID"), 0, 0);
        form.add(customerIdField, 1, 0);
        form.add(whiteLabel("Account type"), 0, 1);
        form.add(accountTypeBox, 1, 1);
        form.add(whiteLabel("Opening deposit (BWP)"), 0, 2);
        form.add(depositField, 1, 2);
        form.add(employerNameLabel, 0, 3);
        form.add(employerNameField, 1, 3);
        form.add(employerAddressLabel, 0, 4);
        form.add(employerAddressField, 1, 4);

        Label hint = new Label("Investment: minimum BWP 500.00.  Cheque: employer details required.");
        hint.setStyle("-fx-text-fill: #d6e4ff; -fx-font-size: 11px;");

        openButton.setStyle("-fx-background-color: #f5b400; -fx-text-fill: #1a1a1a; "
                + "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 20;");
        backButton.setStyle("-fx-background-color: #d6e4ff; -fx-text-fill: #1a1a1a; "
                + "-fx-background-radius: 6; -fx-padding: 8 20;");

        HBox buttons = new HBox(12, openButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        getChildren().addAll(title, form, hint, buttons, messageLabel);
    }

    private static Label whiteLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;");
        return label;
    }

    // Pure screen behaviour: show or hide the employer rows
    private void setEmployerFieldsVisible(boolean visible) {
        employerNameLabel.setVisible(visible);
        employerNameLabel.setManaged(visible);
        employerNameField.setVisible(visible);
        employerNameField.setManaged(visible);
        employerAddressLabel.setVisible(visible);
        employerAddressLabel.setManaged(visible);
        employerAddressField.setVisible(visible);
        employerAddressField.setManaged(visible);
    }

    // ----- what the controller can read from the form -----
    public String getCustomerId() { return customerIdField.getText().trim(); }
    public String getAccountType() { return accountTypeBox.getValue(); }
    public String getOpeningDeposit() { return depositField.getText().trim(); }
    public String getEmployerName() { return employerNameField.getText().trim(); }
    public String getEmployerAddress() { return employerAddressField.getText().trim(); }

    // ----- what the controller can tell the screen -----
    public void setOnOpenAction(EventHandler<ActionEvent> handler) {
        openButton.setOnAction(handler);
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
        customerIdField.clear();
        accountTypeBox.setValue(null);
        depositField.clear();
        employerNameField.clear();
        employerAddressField.clear();
        setEmployerFieldsVisible(false);
    }
}
