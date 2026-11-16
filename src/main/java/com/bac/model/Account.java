package com.bac.model;

import com.bac.exception.AccountOpeningException;
import com.bac.exception.BankingException;
import com.bac.exception.InsufficientFundsException;
import com.bac.exception.InvalidAmountException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ABSTRACTION
public abstract class Account {

    private static int accountCounter = 100001;

    // ENCAPSULATION
    private final String accountNumber;
    private final Customer owner;
    private final String branch;
    private final LocalDate dateOpened;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();

    protected Account(Customer owner, String branch, double openingDeposit) throws BankingException {
        if (owner == null) {
            throw new AccountOpeningException("An account cannot exist without a customer");
        }
        if (branch == null || branch.isBlank()) {
            throw new AccountOpeningException("Branch cannot be empty");
        }
        if (openingDeposit < 0) {
            throw new InvalidAmountException("Opening deposit cannot be negative");
        }

        this.accountNumber = "ACC" + accountCounter++;
        this.owner = owner;
        this.branch = branch.trim();
        this.dateOpened = LocalDate.now();
        this.balance = 0;

        if (openingDeposit > 0) {
            credit(openingDeposit, TransactionType.DEPOSIT, "Opening deposit");
        }

        owner.addAccount(this);
    }

    // Used when loading a saved account from the database
    protected Account(String accountNumber, Customer owner, String branch,
                      LocalDate dateOpened, double balance, List<Transaction> history) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.branch = branch;
        this.dateOpened = dateOpened;
        this.balance = balance;
        this.transactions.addAll(history);

        // Keep the account counter ahead of loaded numbers
        int number = Integer.parseInt(accountNumber.substring(3));
        if (number >= accountCounter) {
            accountCounter = number + 1;
        }

        owner.addAccount(this);
    }

    // OVERLOAD
    public void deposit(double amount) throws BankingException {
        deposit(amount, "Cash deposit");
    }

    public void deposit(double amount, String description) throws BankingException {
        requirePositive(amount);
        credit(amount, TransactionType.DEPOSIT, description);
    }

    // Withdrawal rules for each account type
    public abstract void withdraw(double amount) throws BankingException;

    public abstract String getAccountType();

    // Shared withdrawal logic that the subclasses reuse
    protected void debit(double amount, String description) throws BankingException {
        requirePositive(amount);
        if (amount > balance) {
            throw new InsufficientFundsException(String.format(
                    "Insufficient funds: balance is BWP %.2f, requested BWP %.2f", balance, amount));
        }
        balance = round(balance - amount);
        transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount, balance, description));
    }

    // Add interest to investment and savings accounts
    protected void creditInterest(double amount) {
        if (amount > 0) {
            credit(amount, TransactionType.INTEREST, "Monthly interest");
        }
    }

    private void credit(double amount, TransactionType type, String description) {
        balance = round(balance + amount);
        transactions.add(new Transaction(type, amount, balance, description));
    }

    protected static void requirePositive(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }

    // Round to 2dp
    protected static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public String getAccountNumber() { return accountNumber; }
    public Customer getOwner() { return owner; }
    public String getBranch() { return branch; }
    public LocalDate getDateOpened() { return dateOpened; }
    public double getBalance() { return balance; }

    // View of history
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] owner=%s branch=%s balance=BWP %.2f",
                getAccountType(), accountNumber, owner.getFullName(), branch, balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account other)) return false;
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
}

