package com.bac.controller;

import com.bac.model.Bank;
import com.bac.model.Customer;
import com.bac.view.RegisterCustomerView;

public class RegisterCustomerController {

    private final RegisterCustomerView view;
    private final Bank bank;

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
            view.showSuccess("Customer registered. Customer ID: " + customer.getCustomerId());
            view.clearFields();
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }
}
