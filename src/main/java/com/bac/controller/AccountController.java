package com.bac.controller;

import com.bac.exception.BankingException;
import com.bac.model.Account;
import com.bac.model.Bank;
import com.bac.model.Transaction;
import com.bac.view.AccountView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final AccountView view;
    private final Bank bank;
    private Account currentAccount; // the account currently on screen

    public AccountController(AccountView view, Navigator navigator, Bank bank) {
        this.view = view;
        this.bank = bank;

        view.setOnFindAction(event -> handleFind());
        view.setOnDepositAction(event -> handleDeposit());
        view.setOnWithdrawAction(event -> handleWithdraw());
        view.setOnBackAction(event -> navigator.showMainMenu());
    }

    private void handleFind() {
        String accountNumber = view.getAccountNumber().toUpperCase();
        if (accountNumber.isEmpty()) {
            view.showError("Please enter an account number");
            return;
        }

        Optional<Account> found = bank.findAccount(accountNumber);
        if (found.isEmpty()) {
            currentAccount = null;
            view.clearDetails();
            view.showError("No account found with number " + accountNumber);
            return;
        }

        currentAccount = found.get();
        refreshScreen();
        view.showSuccess("Account found");
    }

    private void handleDeposit() {
        Double amount = readAmount();
        if (amount == null) {
            return;
        }
        try {
            currentAccount.deposit(amount);
            view.showSuccess("Deposit successful");
            view.clearAmount();
            refreshScreen();
        } catch (BankingException e) {
            view.showError(e.getMessage());
        }
    }

    private void handleWithdraw() {
        Double amount = readAmount();
        if (amount == null) {
            return;
        }
        try {
            currentAccount.withdraw(amount);
            view.showSuccess("Withdrawal successful");
            view.clearAmount();
            refreshScreen();
        } catch (BankingException e) {
            // e.g. Savings accounts refuse, or not enough money
            view.showError(e.getMessage());
        }
    }

    // Checks an account is selected and the amount is a number.
    // Returns the amount, or null (after showing an error) if something is wrong.
    private Double readAmount() {
        if (currentAccount == null) {
            view.showError("Find an account first");
            return null;
        }
        String text = view.getAmount();
        if (text.isEmpty()) {
            view.showError("Please enter an amount");
            return null;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            view.showError("Amount must be a number, e.g. 250.00");
            return null;
        }
    }

    // Sends the current account's details and history to the view
    private void refreshScreen() {
        view.showAccountDetails(
                currentAccount.getAccountType(),
                currentAccount.getOwner().getFullName(),
                String.format("BWP %.2f", currentAccount.getBalance()));

        List<Transaction> transactions = currentAccount.getTransactions();
        List<String> lines = new ArrayList<>();
        for (int i = transactions.size() - 1; i >= 0; i--) { // newest first
            Transaction t = transactions.get(i);
            lines.add(String.format("%s | %-10s | BWP %9.2f | Balance: BWP %9.2f | %s",
                    t.getTimestamp().format(TIME_FORMAT), t.getType(), t.getAmount(),
                    t.getBalanceAfter(), t.getDescription()));
        }
        view.setHistory(lines);
    }
}
