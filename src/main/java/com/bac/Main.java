package com.bac;

import com.bac.exception.BankingException;
import com.bac.model.Account;
import com.bac.model.Bank;
import com.bac.model.ChequeAccount;
import com.bac.model.Customer;
import com.bac.model.InvestmentAccount;
import com.bac.model.SavingsAccount;
import com.bac.model.Transaction;

public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank("Bank Gaborone", "Francistown Main");

        try {
            System.out.println("1. Register a customer and open all three account types");
            Customer customer = bank.registerCustomer("Olerato", "Molefe", "Plot 954, Modipane");
            SavingsAccount savings = bank.openSavingsAccount(customer, 1000.00);
            InvestmentAccount investment = bank.openInvestmentAccount(customer, 500.00);
            ChequeAccount cheque = bank.openChequeAccount(customer, 0, "Parts Sales Botswana", "Phakalane, Gaborone");
            System.out.println(customer);
            for (Account account : customer.getAccounts()) {
                System.out.println("  " + account);
            }

            System.out.println("2. Deposits");
            savings.deposit(250.00);
            cheque.deposit(8000.00, "Salary - November");
            System.out.println("Savings balance: " + money(savings.getBalance()));
            System.out.println("Cheque balance:  " + money(cheque.getBalance()));

            System.out.println("3. Withdrawals");
            investment.withdraw(200.00);
            cheque.withdraw(1500.00);
            System.out.println("Investment balance: " + money(investment.getBalance()));
            System.out.println("Cheque balance:     " + money(cheque.getBalance()));

            System.out.println("4. Monthly interest");
            System.out.println("Savings interest due:    " + money(savings.calculateMonthlyInterest()));
            System.out.println("Investment interest due: " + money(investment.calculateMonthlyInterest()));
            int paid = bank.payMonthlyInterest();
            System.out.println("Interest paid to " + paid + " accounts (cheque skipped)");
            System.out.println("Savings balance:    " + money(savings.getBalance()));
            System.out.println("Investment balance: " + money(investment.getBalance()));
            System.out.println("Cheque balance:     " + money(cheque.getBalance()));

            System.out.println("5. Transaction history (Investment)");
            for (Transaction t : investment.getTransactions()) {
                System.out.println("  " + t);
            }

            System.out.println("\nTotal across all accounts:" + money(customer.getTotalBalance()));

            System.out.println("6. Rules being enforced");
            attempt("Withdraw from Savings", () -> savings.withdraw(100));
            attempt("Overdraw Investment", () -> investment.withdraw(999999));
            attempt("Deposit negative amount", () -> cheque.deposit(-50));
            attempt("Open Investment with BWP 100", () -> bank.openInvestmentAccount(customer, 100));
            attempt("Open Cheque with no employer", () -> bank.openChequeAccount(customer, 0, "", ""));
            attempt("Open account with no customer", () -> new SavingsAccount(null, "Gaborone Main", 0));

        } catch (BankingException e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static String money(double amount) {
        return "BWP"+String.format("%.2f", amount);
    }

    // Runs an action that SHOULD fail, and prints the rejection message
    private static void attempt(String label, BankingAction action) {
        try {
            action.run();
            System.out.println("  [!] " + label + " -> unexpectedly succeeded");
        } catch (BankingException e) {
            System.out.println("  [OK] " + label + " -> rejected: " + e.getMessage());
        }
    }

    @FunctionalInterface
    private interface BankingAction {
        void run() throws BankingException;
    }
}