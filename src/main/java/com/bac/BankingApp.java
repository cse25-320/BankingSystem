package com.bac;

import com.bac.controller.Navigator;
import com.bac.dao.AccountDAO;
import com.bac.dao.CustomerDAO;
import com.bac.dao.DatabaseManager;
import com.bac.model.Bank;
import com.bac.model.Customer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.SQLException;

public class BankingApp extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseManager.initialiseSchema();

        Bank bank = new Bank("Bank Gaborone", "Gaborone Main");
        loadSavedData(bank);

        Navigator navigator = new Navigator(stage, bank);
        navigator.showLogin();
    }

    // Rebuilds the bank's customers and accounts from the database
    private void loadSavedData(Bank bank) {
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        try {
            for (Customer customer : customerDAO.findAll()) {
                accountDAO.findByCustomer(customer); // attaches the accounts to the customer
                bank.addExistingCustomer(customer);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not load data from the database", e);
        }
    }
}
