package com.bac.model;

import com.bac.exception.AccountOpeningException;
import com.bac.exception.BankingException;

public class InvestmentAccount extends Account implements InterestBearing {

    public static final double MONTHLY_INTEREST_RATE = 5.0; // percent
    public static final double MINIMUM_OPENING_DEPOSIT = 500.00;

    public InvestmentAccount(Customer owner, String branch, double openingDeposit) throws BankingException {
        // The check runs BEFORE the parent constructor, so a bad account is never created
        super(owner, branch, requireMinimumDeposit(openingDeposit));
    }

    private static double requireMinimumDeposit(double openingDeposit) throws AccountOpeningException {
        if (openingDeposit < MINIMUM_OPENING_DEPOSIT) {
            throw new AccountOpeningException(String.format(
                    "An Investment account needs an opening deposit of at least BWP %.2f",
                    MINIMUM_OPENING_DEPOSIT));
        }
        return openingDeposit;
    }

    // OVERRIDING: withdrawals are allowed here
    @Override
    public void withdraw(double amount) throws BankingException {
        debit(amount, "Cash withdrawal");
    }

    @Override
    public String getAccountType() {
        return "Investment";
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
