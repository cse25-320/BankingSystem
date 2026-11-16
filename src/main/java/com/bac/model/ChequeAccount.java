package com.bac.model;

import com.bac.exception.AccountOpeningException;
import com.bac.exception.BankingException;
import java.time.LocalDate;
import java.util.List;

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

    // Used when loading from the database
    public ChequeAccount(String accountNumber, Customer owner, String branch,
                         LocalDate dateOpened, double balance, List<Transaction> history,
                         String employerName, String employerAddress) {
        super(accountNumber, owner, branch, dateOpened, balance, history);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
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

    // adding the employer details to the parent's version
    @Override
    public String toString() {
        return super.toString() + " employer=" + employerName + " (" + employerAddress + ")";
    }
}
