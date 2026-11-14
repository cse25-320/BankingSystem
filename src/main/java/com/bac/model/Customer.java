package com.bac.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {

    private static int idCounter = 1001;

    private final String customerId;
    private String firstName;
    private String surname;
    private String address;
    private final List<Account> accounts = new ArrayList<>();

    // Full constructor
    public Customer(String firstName, String surname, String address) {
        this.customerId = "CUST" + idCounter++;
        setFirstName(firstName);
        setSurname(surname);
        setAddress(address);
    }

    // OVERLOADED constructor: address can be added later
    public Customer(String firstName, String surname) {
        this(firstName, surname, "Not provided");
    }

    // Package-private: only an Account registers itself with its owner
    void addAccount(Account account) {
        if (!accounts.contains(account)) {
            accounts.add(account);
        }
    }

    // Read-only view, so outside code can't change the list (encapsulation)
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getFullName() { return firstName + " " + surname; }

    public void setFirstName(String firstName) {
        this.firstName = requireText(firstName, "First name");
    }

    public void setSurname(String surname) {
        this.surname = requireText(surname, "Surname");
    }

    public void setAddress(String address) {
        this.address = requireText(address, "Address");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be empty");
        }
        return value.trim();
    }

    @Override
    public String toString() {
        return customerId + " - " + getFullName() + " (" + accounts.size() + " account(s))";
    }
}

