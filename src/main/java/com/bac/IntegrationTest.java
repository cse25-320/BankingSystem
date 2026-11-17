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
import com.bac.model.SavingsAccount;

import java.sql.SQLException;

public class IntegrationTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        DatabaseManager.initialiseSchema();
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();

        // 1. Start up like the real app: load saved data (this also moves the ID counters forward)
        Bank bank = loadBank(customerDAO, accountDAO);
        int customersBefore = bank.getCustomers().size();
        System.out.println("Start-up: " + customersBefore + " customers loaded from the database");

        // 2. Register a customer and open all three account types, saving each one
        Customer customer = bank.registerCustomer("Integration", "Test", "Plot 1, Gaborone");
        customerDAO.insert(customer);

        SavingsAccount savings = bank.openSavingsAccount(customer, 1000.00);
        accountDAO.insert(savings);
        InvestmentAccount investment = bank.openInvestmentAccount(customer, 2000.00);
        accountDAO.insert(investment);
        ChequeAccount cheque = bank.openChequeAccount(customer, 0, "Test Employer", "Main Mall, Gaborone");
        accountDAO.insert(cheque);

        // 3. Deposit, withdraw and pay interest, saving after each change
        cheque.deposit(5000.00, "Salary");
        accountDAO.saveChanges(cheque);
        cheque.withdraw(1500.00);
        accountDAO.saveChanges(cheque);
        investment.withdraw(500.00);
        accountDAO.saveChanges(investment);
        savings.payMonthlyInterest();
        accountDAO.saveChanges(savings);
        investment.payMonthlyInterest();
        accountDAO.saveChanges(investment);

        // 4. Reload everything from the database into a brand-new Bank
        Bank reloaded = loadBank(customerDAO, accountDAO);

        Account s = reloaded.findAccount(savings.getAccountNumber()).orElse(null);
        Account i = reloaded.findAccount(investment.getAccountNumber()).orElse(null);
        Account c = reloaded.findAccount(cheque.getAccountNumber()).orElse(null);

        System.out.println();
        check("Customer was saved and reloaded", reloaded.getCustomers().size() == customersBefore + 1);
        check("Savings account reloaded", s != null);
        check("Investment account reloaded", i != null);
        check("Cheque account reloaded", c != null);
        check("Savings balance is 1000.50 (1000 + 0.50 interest)", s != null && balanceIs(s, 1000.50));
        check("Investment balance is 1575.00 (2000 - 500 + 75 interest)", i != null && balanceIs(i, 1575.00));
        check("Cheque balance is 3500.00 (5000 - 1500)", c != null && balanceIs(c, 3500.00));
        check("Savings history has 2 rows", s != null && s.getTransactions().size() == 2);
        check("Investment history has 3 rows", i != null && i.getTransactions().size() == 3);
        check("Cheque history has 2 rows", c != null && c.getTransactions().size() == 2);
        check("Cheque account kept its employer details",
                c instanceof ChequeAccount cq && cq.getEmployerName().equals("Test Employer"));

        boolean refused = false;
        try {
            s.withdraw(10);
        } catch (BankingException e) {
            refused = true;
        }
        check("Reloaded Savings account still refuses withdrawals", refused);

        // 5. Clean up so the sample data is left as it was
        accountDAO.delete(savings.getAccountNumber());
        accountDAO.delete(investment.getAccountNumber());
        accountDAO.delete(cheque.getAccountNumber());
        customerDAO.delete(customer.getCustomerId());
        check("Clean-up: database is back to " + customersBefore + " customers",
                customerDAO.findAll().size() == customersBefore);

        System.out.println();
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
    }

    // Same steps the app performs at start-up
    private static Bank loadBank(CustomerDAO customerDAO, AccountDAO accountDAO) throws SQLException {
        Bank bank = new Bank("Bank Gaborone", "Gaborone Main");
        for (Customer customer : customerDAO.findAll()) {
            accountDAO.findByCustomer(customer);
            bank.addExistingCustomer(customer);
        }
        return bank;
    }

    private static boolean balanceIs(Account account, double expected) {
        return Math.abs(account.getBalance() - expected) < 0.001;
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  PASS - " + name);
        } else {
            failed++;
            System.out.println("  FAIL - " + name);
        }
    }
}
