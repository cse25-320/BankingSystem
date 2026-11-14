package com.bac.controller;

import com.bac.exception.BankingException;
import com.bac.model.Bank;
import com.bac.view.MainMenuView;

public class MainMenuController {

    private final MainMenuView view;
    private final Navigator navigator;
    private final Bank bank;

    public MainMenuController(MainMenuView view, Navigator navigator, Bank bank) {
        this.view = view;
        this.navigator = navigator;
        this.bank = bank;

        view.setWelcomeText("Welcome, Teller");
        view.setOnPayInterestAction(event -> handlePayInterest());
        view.setOnLogoutAction(event -> navigator.showLogin());
        view.setOnRegisterCustomerAction(event -> navigator.showRegisterCustomer());
        view.setOnOpenAccountAction(event -> navigator.showOpenAccount());
        view.setOnViewAccountsAction(event -> navigator.showAccountView());
    }

    private void handlePayInterest() {
        try {
            int paid = bank.payMonthlyInterest();
            view.showInfo("Monthly interest paid to " + paid + " account(s).");
        } catch (BankingException e) {
            view.showInfo("Could not pay interest: " + e.getMessage());
        }
    }
}
