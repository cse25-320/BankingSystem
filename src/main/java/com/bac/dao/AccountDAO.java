package com.bac.dao;

import com.bac.model.Account;
import com.bac.model.ChequeAccount;
import com.bac.model.Customer;
import com.bac.model.InvestmentAccount;
import com.bac.model.SavingsAccount;
import com.bac.model.Transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAO {

    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    // CREATE: saves the account row and any transactions it already has (e.g. the opening deposit)
    public void insert(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, branch, "
                + "date_opened, balance, employer_name, employer_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getAccountNumber());
            statement.setString(2, account.getOwner().getCustomerId());
            statement.setString(3, account.getAccountType());
            statement.setString(4, account.getBranch());
            statement.setDate(5, Date.valueOf(account.getDateOpened()));
            statement.setDouble(6, account.getBalance());
            if (account instanceof ChequeAccount cheque) {
                statement.setString(7, cheque.getEmployerName());
                statement.setString(8, cheque.getEmployerAddress());
            } else {
                statement.setNull(7, java.sql.Types.VARCHAR);
                statement.setNull(8, java.sql.Types.VARCHAR);
            }
            statement.executeUpdate();
        }
        for (Transaction transaction : account.getTransactions()) {
            transactionDAO.insert(account.getAccountNumber(), transaction);
        }
    }

    // READ one: rebuilds the account together with its owner and history
    public Optional<Account> findByNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            try (ResultSet rows = statement.executeQuery()) {
                if (!rows.next()) {
                    return Optional.empty();
                }
                Optional<Customer> owner = customerDAO.findById(rows.getString("customer_id"));
                if (owner.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(fromRow(rows, owner.get()));
            }
        }
    }

    // READ many: all accounts of one customer (they get attached to that customer object)
    public List<Account> findByCustomer(Customer owner) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY account_number";
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, owner.getCustomerId());
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    accounts.add(fromRow(rows, owner));
                }
            }
        }
        return accounts;
    }

    // UPDATE: save the current balance
    public void updateBalance(Account account) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getAccountNumber());
            statement.executeUpdate();
        }
    }

    // Saves any transactions not yet in the database, then the new balance.
// Call it after a deposit, withdrawal or interest payment.
    public void saveChanges(Account account) throws SQLException {
        int alreadySaved = transactionDAO.countByAccount(account.getAccountNumber());
        List<Transaction> history = account.getTransactions();
        for (int i = alreadySaved; i < history.size(); i++) {
            transactionDAO.insert(account.getAccountNumber(), history.get(i));
        }
        updateBalance(account);
    }

    // DELETE: history first (foreign key), then the account itself
    public void delete(String accountNumber) throws SQLException {
        transactionDAO.deleteByAccount(accountNumber);
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.executeUpdate();
        }
    }

    // Turns one row into the right kind of account (using the restore constructors)
    private Account fromRow(ResultSet row, Customer owner) throws SQLException {
        String number = row.getString("account_number");
        String type = row.getString("account_type");
        String branch = row.getString("branch");
        LocalDate dateOpened = row.getDate("date_opened").toLocalDate();
        double balance = row.getDouble("balance");
        List<Transaction> history = transactionDAO.findByAccount(number);

        return switch (type) {
            case "Savings" -> new SavingsAccount(number, owner, branch, dateOpened, balance, history);
            case "Investment" -> new InvestmentAccount(number, owner, branch, dateOpened, balance, history);
            case "Cheque" -> new ChequeAccount(number, owner, branch, dateOpened, balance, history,
                    row.getString("employer_name"), row.getString("employer_address"));
            default -> throw new SQLException("Unknown account type: " + type);
        };
    }
}
