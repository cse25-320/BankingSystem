package com.bac;

import com.bac.dao.AccountDAO;
import com.bac.dao.CustomerDAO;
import com.bac.dao.DatabaseManager;
import com.bac.exception.BankingException;
import com.bac.model.Account;
import com.bac.model.Bank;
import com.bac.model.ChequeAccount;
import com.bac.model.Customer;
import com.bac.model.InvestmentAccount;

import java.sql.SQLException;

public class SampleData {

    public static void main(String[] args) throws SQLException, BankingException {
        DatabaseManager.initialiseSchema();
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();

        if (!customerDAO.findAll().isEmpty()) {
            System.out.println("The database already has customers, so no sample data was added.");
            return;
        }

        Bank bank = new Bank("Bank Gaborone", "Gaborone Main");

        // ----- 10 customers -----
        Customer[] c = {
                bank.registerCustomer("Kabo", "Mokgosi", "Plot 1021, Block 8, Gaborone"),
                bank.registerCustomer("Neo", "Sebina", "Plot 455, Phakalane, Gaborone"),
                bank.registerCustomer("Thato", "Molefhi", "Plot 78, Francistown"),
                bank.registerCustomer("Lesedi", "Dikgang", "Plot 310, Maun"),
                bank.registerCustomer("Tshepo", "Ramotswa", "Plot 92, Block 5, Gaborone"),
                bank.registerCustomer("Kagiso", "Seretse", "Plot 14, Serowe"),
                bank.registerCustomer("Lorato", "Modise", "Plot 267, Tlokweng"),
                bank.registerCustomer("Onkabetse", "Kgari", "Plot 530, Mogoditshane"),
                bank.registerCustomer("Boitumelo", "Phiri", "Plot 63, Lobatse"),
                bank.registerCustomer("Mpho", "Tau", "Plot 205, Palapye")
        };

        // ----- a Savings account for everyone (10) -----
        double[] savingsDeposits = {1500, 2500, 800, 4000, 1200, 3000, 950, 5000, 2200, 700};
        for (int i = 0; i < c.length; i++) {
            bank.openSavingsAccount(c[i], savingsDeposits[i]).deposit(250.00, "Top-up");
        }

        // ----- 4 Cheque accounts (employer details required) -----
        ChequeAccount ch0 = bank.openChequeAccount(c[0], 0, "Botswana Telecom", "Main Mall, Gaborone");
        ch0.deposit(8500.00, "Salary - November");
        ch0.withdraw(1200.00);

        ChequeAccount ch2 = bank.openChequeAccount(c[2], 0, "Debswana", "Orapa House, Gaborone");
        ch2.deposit(12000.00, "Salary - November");
        ch2.withdraw(3500.00);

        ChequeAccount ch4 = bank.openChequeAccount(c[4], 0, "Choppies", "Broadhurst, Gaborone");
        ch4.deposit(6000.00, "Salary - November");
        ch4.withdraw(900.00);

        ChequeAccount ch7 = bank.openChequeAccount(c[7], 0, "Ministry of Health", "Government Enclave, Gaborone");
        ch7.deposit(9500.00, "Salary - November");
        ch7.withdraw(2000.00);

        // ----- 3 Investment accounts (minimum BWP 500) -----
        InvestmentAccount inv1 = bank.openInvestmentAccount(c[1], 5000.00);
        inv1.withdraw(500.00);

        InvestmentAccount inv3 = bank.openInvestmentAccount(c[3], 10000.00);
        inv3.deposit(2000.00, "Extra investment");

        InvestmentAccount inv6 = bank.openInvestmentAccount(c[6], 2500.00);
        inv6.withdraw(300.00);

        // ----- one month of interest (Savings and Investment only) -----
        bank.payMonthlyInterest();

        // ----- save everything to the database -----
        int accountCount = 0;
        int transactionCount = 0;
        for (Customer customer : bank.getCustomers()) {
            customerDAO.insert(customer);
            for (Account account : customer.getAccounts()) {
                accountDAO.insert(account);
                accountCount++;
                transactionCount += account.getTransactions().size();
            }
        }

        System.out.println("Sample data saved:");
        System.out.println("  Customers:    " + bank.getCustomers().size());
        System.out.println("  Accounts:     " + accountCount);
        System.out.println("  Transactions: " + transactionCount);
    }
}
