package com.bac.model;

import com.bac.exception.AccountOpeningException;
import com.bac.exception.BankingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Bank {

    private final String name;
    private final String branch;
    private final List<Customer> customers = new ArrayList<>();

    public Bank(String name, String branch) {
        this.name = name;
        this.branch = branch;
    }

    // Customer

    public Customer registerCustomer(String firstName, String surname, String address) {
        Customer customer = new Customer(firstName, surname, address);
        customers.add(customer);
        return customer;
    }

    public Optional<Customer> findCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return Optional.of(customer);
            }
        }
        return Optional.empty();
    }

    public Optional<Account> findAccount(String accountNumber) {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    return Optional.of(account);
                }
            }
        }
        return Optional.empty();
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    // opening accounts for customer

    public SavingsAccount openSavingsAccount(Customer customer, double openingDeposit)
            throws BankingException {
        requireRegistered(customer);
        return new SavingsAccount(customer, branch, openingDeposit);
    }

    public InvestmentAccount openInvestmentAccount(Customer customer, double openingDeposit)
            throws BankingException {
        requireRegistered(customer);
        return new InvestmentAccount(customer, branch, openingDeposit);
    }

    public ChequeAccount openChequeAccount(Customer customer, double openingDeposit,
                                           String employerName, String employerAddress)
            throws BankingException {
        requireRegistered(customer);
        return new ChequeAccount(customer, branch, openingDeposit, employerName, employerAddress);
    }

    private void requireRegistered(Customer customer) throws AccountOpeningException {
        if (customer == null || !customers.contains(customer)) {
            throw new AccountOpeningException("Customer is not registered with " + name);
        }
    }

    // monthly interesr

    public int payMonthlyInterest() throws BankingException {
        int paid = 0;
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account instanceof InterestBearing interestBearing) {
                    interestBearing.payMonthlyInterest();
                    paid++;
                }
            }
        }
        return paid;
    }

    public String getName() { return name; }
    public String getBranch() { return branch; }
}
