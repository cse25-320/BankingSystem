package com.bac.model;

import com.bac.exception.AccountOpeningException;
import com.bac.exception.BankingException;

public class ChequeAccount extends Account {

    private final String employerName;
    private final String employerAddress;

    public ChequeAccount(Customer owner, String branch, double openingDeposit,
                         String employerName, String employerAddress) throws BankingException {
        // Employment check runs BEFORE the parent constructor
        super(owner, branch, requireEmployment(openingDeposit, employerName, employerAddress));
        this.employerName = employerName.trim();
        this.employerAddress = employerAddress.trim();
    }

    private static double requireEmployment(double openingDeposit, String employerName,
                                            String employerAddress) throws AccountOpeningException {
        if (employerName == null || employerName.isBlank()
                || employerAddress == null || employerAddress.isBlank()) {
            throw new AccountOpeningException(
                    "A Cheque account can only be opened for a working customer: "
                            + "employer name and address are required");
        }
        return openingDeposit;
    }

    @Override
    public void withdraw(double amount) throws BankingException {
        debit(amount, "Cash withdrawal");
    }

    @Override
    public String getAccountType() {
        return "Cheque";
    }

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }

    // OVERRIDING toString and adding the employer details to the parent's version
    @Override
    public String toString() {
        return super.toString() + " employer=" + employerName + " (" + employerAddress + ")";
    }
}
