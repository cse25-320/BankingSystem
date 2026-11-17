package com.bac.controller;

import com.bac.dao.AccountDAO;
import com.bac.exception.BankingException;
import com.bac.model.Account;
import com.bac.model.Bank;
import com.bac.model.Customer;
import com.bac.view.OpenAccountView;

import java.sql.SQLException;
import java.util.Optional;

public class OpenAccountController {

    private final OpenAccountView view;
    private final Bank bank;
    private final AccountDAO accountDAO = new AccountDAO();

    public OpenAccountController(OpenAccountView view, Navigator navigator, Bank bank) {
        this.view = view;
        this.bank = bank;

        view.setOnOpenAction(event -> handleOpenAccount());
        view.setOnBackAction(event -> navigator.showMainMenu());
    }

    private void handleOpenAccount() {
        String customerId = view.getCustomerId().toUpperCase();
        String accountType = view.getAccountType();
        String depositText = view.getOpeningDeposit();

        if (customerId.isEmpty()) {
            view.showError("Please enter the customer ID");
            return;
        }
        if (accountType == null) {
            view.showError("Please select an account type");
            return;
        }

        // Turn the typed text into a number (an empty box means no opening deposit)
        double openingDeposit = 0;
        if (!depositText.isEmpty()) {
            try {
                openingDeposit = Double.parseDouble(depositText);
            } catch (NumberFormatException e) {
                view.showError("Opening deposit must be a number, e.g. 500.00");
                return;
            }
        }

        Optional<Customer> found = bank.findCustomer(customerId);
        if (found.isEmpty()) {
            view.showError("No customer found with ID " + customerId);
            return;
        }
        Customer customer = found.get();

        try {
            Account account;
            switch (accountType) {
                case "Savings" -> account = bank.openSavingsAccount(customer, openingDeposit);
                case "Investment" -> account = bank.openInvestmentAccount(customer, openingDeposit);
                case "Cheque" -> account = bank.openChequeAccount(customer, openingDeposit,
                        view.getEmployerName(), view.getEmployerAddress());
                default -> {
                    view.showError("Unknown account type");
                    return;
                }
            }
            accountDAO.insert(account); // save to the database
            view.showSuccess(accountType + " account opened. Account number: " + account.getAccountNumber());
            view.clearFields();
        } catch (BankingException e) {
            // The model's rules (minimum deposit, employer required...) produce these messages
            view.showError(e.getMessage());
        } catch (SQLException e) {
            view.showError("Could not save to the database: " + e.getMessage());
        }
    }
}