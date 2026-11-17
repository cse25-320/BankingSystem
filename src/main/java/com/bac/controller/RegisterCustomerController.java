package com.bac.controller;

import com.bac.dao.CustomerDAO;
import com.bac.model.Bank;
import com.bac.model.Customer;
import com.bac.view.RegisterCustomerView;

import java.sql.SQLException;

public class RegisterCustomerController {

    private final RegisterCustomerView view;
    private final Bank bank;
    private final CustomerDAO customerDAO = new CustomerDAO();

    public RegisterCustomerController(RegisterCustomerView view, Navigator navigator, Bank bank) {
        this.view = view;
        this.bank = bank;

        view.setOnRegisterAction(event -> handleRegister());
        view.setOnBackAction(event -> navigator.showMainMenu());
    }

    private void handleRegister() {
        String firstName = view.getFirstName();
        String surname = view.getSurname();
        String address = view.getAddress();

        if (firstName.isEmpty() || surname.isEmpty() || address.isEmpty()) {
            view.showError("Please fill in all the fields");
            return;
        }

        try {
            Customer customer = bank.registerCustomer(firstName, surname, address);
            customerDAO.insert(customer); // save to the database
            view.showSuccess("Customer registered. Customer ID: " + customer.getCustomerId());
            view.clearFields();
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (SQLException e) {
            view.showError("Could not save to the database: " + e.getMessage());
        }
    }
}