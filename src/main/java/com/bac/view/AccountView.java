package com.bac.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

// BOUNDARY CLASS: account details, deposits, withdrawals and history. No business logic.
public class AccountView extends VBox {

    private final TextField accountNumberField = new TextField();
    private final Button findButton = new Button("Find");
    private final Label typeLabel = new Label("-");
    private final Label ownerLabel = new Label("-");
    private final Label balanceLabel = new Label("-");
    private final TextField amountField = new TextField();
    private final Button depositButton = new Button("Deposit");
    private final Button withdrawButton = new Button("Withdraw");
    private final TextArea historyArea = new TextArea();
    private final Button backButton = new Button("Back to Menu");
    private final Label messageLabel = new Label();

    public AccountView() {
        setSpacing(16);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72, #2a5298);");

        Label title = new Label("Accounts & Transactions");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        String focusStyle = "-fx-focus-color: #f5b400; -fx-faint-focus-color: transparent;";
        accountNumberField.setStyle(focusStyle);
        amountField.setStyle(focusStyle);
        historyArea.setStyle(focusStyle);

        // --- find an account ---
        accountNumberField.setPromptText("Account number");
        styleGoldButton(findButton);
        HBox findRow = new HBox(10, whiteLabel("Account number"), accountNumberField, findButton);
        findRow.setAlignment(Pos.CENTER);

        // --- account details ---
        typeLabel.setStyle("-fx-text-fill: #f5b400; -fx-font-weight: bold;");
        ownerLabel.setStyle("-fx-text-fill: #f5b400; -fx-font-weight: bold;");
        balanceLabel.setStyle("-fx-text-fill: #f5b400; -fx-font-weight: bold; -fx-font-size: 16px;");

        GridPane details = new GridPane();
        details.setHgap(12);
        details.setVgap(8);
        details.setAlignment(Pos.CENTER);
        details.add(whiteLabel("Account type:"), 0, 0);
        details.add(typeLabel, 1, 0);
        details.add(whiteLabel("Owner:"), 0, 1);
        details.add(ownerLabel, 1, 1);
        details.add(whiteLabel("Balance:"), 0, 2);
        details.add(balanceLabel, 1, 2);

        // --- deposit / withdraw ---
        amountField.setPromptText("Amount");
        styleGoldButton(depositButton);
        styleGoldButton(withdrawButton);
        HBox amountRow = new HBox(10, whiteLabel("Amount (BWP)"), amountField, depositButton, withdrawButton);
        amountRow.setAlignment(Pos.CENTER);

        // --- history ---
        historyArea.setEditable(false);
        historyArea.setPrefRowCount(8);
        historyArea.setStyle(focusStyle + " -fx-font-family: 'Consolas', monospace;");

        backButton.setStyle("-fx-background-color: #d6e4ff; -fx-text-fill: #1a1a1a; "
                + "-fx-background-radius: 6; -fx-padding: 8 20;");

        getChildren().addAll(title, findRow, details, amountRow,
                whiteLabel("Transaction history"), historyArea, messageLabel, backButton);
    }

    private static Label whiteLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;");
        return label;
    }

    private static void styleGoldButton(Button button) {
        button.setStyle("-fx-background-color: #f5b400; -fx-text-fill: #1a1a1a; "
                + "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 16;");
    }

    // ----- what the controller can read from the screen -----
    public String getAccountNumber() { return accountNumberField.getText().trim(); }
    public String getAmount() { return amountField.getText().trim(); }

    // ----- what the controller can tell the screen -----
    public void setOnFindAction(EventHandler<ActionEvent> handler) { findButton.setOnAction(handler); }
    public void setOnDepositAction(EventHandler<ActionEvent> handler) { depositButton.setOnAction(handler); }
    public void setOnWithdrawAction(EventHandler<ActionEvent> handler) { withdrawButton.setOnAction(handler); }
    public void setOnBackAction(EventHandler<ActionEvent> handler) { backButton.setOnAction(handler); }

    public void showAccountDetails(String type, String owner, String balance) {
        typeLabel.setText(type);
        ownerLabel.setText(owner);
        balanceLabel.setText(balance);
    }

    public void setHistory(List<String> lines) {
        historyArea.setText(String.join("\n", lines));
    }

    public void clearAmount() {
        amountField.clear();
    }

    public void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: #a8f0b8; -fx-font-weight: bold;");
        messageLabel.setText(message);
    }

    public void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: #ffb4a9; -fx-font-weight: bold;");
        messageLabel.setText(message);
    }

    public void clearDetails() {
        showAccountDetails("-", "-", "-");
        historyArea.clear();
    }
}