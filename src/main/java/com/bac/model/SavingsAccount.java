package com.bac.model;

import com.bac.exception.BankingException;
import com.bac.exception.WithdrawalNotAllowedException;

// INHERITANCE: SavingsAccount IS-A Account
// INTERFACE: it also implements InterestBearing
public class SavingsAccount extends Account implements InterestBearing {

    public static final double MONTHLY_INTEREST_RATE = 0.05; // percent

    public SavingsAccount(Customer owner, String branch, double openingDeposit) throws BankingException {
        super(owner, branch, openingDeposit);
    }

    // OVERLOADED constructor: open with no initial deposit
    public SavingsAccount(Customer owner, String branch) throws BankingException {
        this(owner, branch, 0);
    }

    // OVERRIDING: savings accounts never allow withdrawals
    @Override
    public void withdraw(double amount) throws BankingException {
        throw new WithdrawalNotAllowedException("Withdrawals are not allowed from a Savings account");
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public double getMonthlyInterestRate() {
        return MONTHLY_INTEREST_RATE;
    }

    @Override
    public double calculateMonthlyInterest() {
        return round(getBalance() * MONTHLY_INTEREST_RATE / 100.0);
    }

    @Override
    public void payMonthlyInterest() throws BankingException {
        creditInterest(calculateMonthlyInterest());
    }
}

