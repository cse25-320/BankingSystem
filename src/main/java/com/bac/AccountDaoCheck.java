package com.bac;

import com.bac.dao.AccountDAO;
import com.bac.dao.CustomerDAO;
import com.bac.dao.DatabaseManager;
import com.bac.dao.TransactionDAO;
import com.bac.exception.BankingException;
import com.bac.model.Account;
import com.bac.model.ChequeAccount;
import com.bac.model.Customer;
import com.bac.model.SavingsAccount;
import com.bac.model.Transaction;

import java.sql.SQLException;
import java.util.List;

public class AccountDaoCheck {

    public static void main(String[] args) throws SQLException, BankingException {
        DatabaseManager.initialiseSchema();
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        // 1. A customer with a Savings and a Cheque account
        Customer customer = new Customer("Test", "User", "Plot 1, Gaborone");
        customerDAO.insert(customer);

        SavingsAccount savings = new SavingsAccount(customer, "Gaborone Main", 1000.00);
        ChequeAccount cheque = new ChequeAccount(customer, "Gaborone Main", 0,
                "Botswana Telecom", "Main Mall, Gaborone");
        accountDAO.insert(savings);
        accountDAO.insert(cheque);
        System.out.println("INSERT -> saved " + savings.getAccountNumber() + " and " + cheque.getAccountNumber());

        // 2. A deposit: save the new transaction and the new balance
        savings.deposit(250.00);
        List<Transaction> history = savings.getTransactions();
        transactionDAO.insert(savings.getAccountNumber(), history.get(history.size() - 1));
        accountDAO.updateBalance(savings);
        System.out.println("UPDATE -> deposit saved");

        // 3. Read it back from the database
        Account loaded = accountDAO.findByNumber(savings.getAccountNumber()).orElseThrow();
        System.out.println("FIND   -> " + loaded);
        System.out.println("         history rows: " + loaded.getTransactions().size());

        Customer reloaded = customerDAO.findById(customer.getCustomerId()).orElseThrow();
        System.out.println("LIST   -> accounts loaded for customer: " + accountDAO.findByCustomer(reloaded).size());

        // 4. Clean up
        accountDAO.delete(savings.getAccountNumber());
        accountDAO.delete(cheque.getAccountNumber());
        customerDAO.delete(customer.getCustomerId());
        System.out.println("DELETE -> still there? "
                + accountDAO.findByNumber(savings.getAccountNumber()).isPresent());
    }
}
