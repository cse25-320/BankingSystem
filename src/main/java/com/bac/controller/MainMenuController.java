package com.bac.controller;

import com.bac.dao.AccountDAO;
import com.bac.exception.BankingException;
import com.bac.model.Account;
import com.bac.model.Bank;
import com.bac.model.Customer;
import com.bac.model.InterestBearing;
import com.bac.view.MainMenuView;

import java.sql.SQLException;

public class MainMenuController {

    private final MainMenuView view;
    private final Bank bank;
    private final AccountDAO accountDAO = new AccountDAO();

    public MainMenuController(MainMenuView view, Navigator navigator, Bank bank) {
        this.view = view;
        this.bank = bank;

        view.setWelcomeText("Welcome, Teller");
        view.setOnRegisterCustomerAction(event -> navigator.showRegisterCustomer());
        view.setOnOpenAccountAction(event -> navigator.showOpenAccount());
        view.setOnViewAccountsAction(event -> navigator.showAccountView());
        view.setOnPayInterestAction(event -> handlePayInterest());
        view.setOnLogoutAction(event -> navigator.showLogin());
    }

    private void handlePayInterest() {
        try {
            int paid = bank.payMonthlyInterest();

            // Save the interest and new balance of every account that earns interest
            for (Customer customer : bank.getCustomers()) {
                for (Account account : customer.getAccounts()) {
                    if (account instanceof InterestBearing) {
                        accountDAO.saveChanges(account);
                    }
                }
            }

            view.showInfo("Monthly interest paid to " + paid + " account(s).");
        } catch (BankingException e) {
            view.showInfo("Could not pay interest: " + e.getMessage());
        } catch (SQLException e) {
            view.showInfo("Could not save to the database: " + e.getMessage());
        }
    }
}