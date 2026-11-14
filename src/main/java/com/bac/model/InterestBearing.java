package com.bac.model;

import com.bac.exception.BankingException;

public interface InterestBearing {

    //Monthly interest rate as a percentage
    double getMonthlyInterest();

    //Works out current month's interest
    double calculateMonthlyInterest();

    //calculates current month's interest and adds it to the balance
    void payMonthlyInterest() throws BankingException;
}
