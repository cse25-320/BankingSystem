package com.bac.dao;

import com.bac.model.Transaction;
import com.bac.model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // CREATE: save one transaction against an account
    public void insert(String accountNumber, Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount, "
                + "balance_after, description, transaction_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.setString(2, transaction.getType().name());
            statement.setDouble(3, transaction.getAmount());
            statement.setDouble(4, transaction.getBalanceAfter());
            statement.setString(5, transaction.getDescription());
            statement.setTimestamp(6, Timestamp.valueOf(transaction.getTimestamp()));
            statement.executeUpdate();
        }
    }

    // READ: an account's history, oldest first
    public List<Transaction> findByAccount(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_id";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    transactions.add(new Transaction(
                            TransactionType.valueOf(rows.getString("transaction_type")),
                            rows.getDouble("amount"),
                            rows.getDouble("balance_after"),
                            rows.getString("description"),
                            rows.getTimestamp("transaction_time").toLocalDateTime()));
                }
            }
        }
        return transactions;
    }

    // How many transactions are already saved for this account
    public int countByAccount(String accountNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM transactions WHERE account_number = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            try (ResultSet rows = statement.executeQuery()) {
                rows.next();
                return rows.getInt(1);
            }
        }
    }

    // DELETE: remove all of an account's history
    public void deleteByAccount(String accountNumber) throws SQLException {
        String sql = "DELETE FROM transactions WHERE account_number = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.executeUpdate();
        }
    }
}
