package com.bac.dao;

import com.bac.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAO {

    // CREATE
    public void insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (customer_id, first_name, surname, address) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getCustomerId());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getSurname());
            statement.setString(4, customer.getAddress());
            statement.executeUpdate();
        }
    }

    // READ one
    public Optional<Customer> findById(String customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customerId);
            try (ResultSet rows = statement.executeQuery()) {
                if (rows.next()) {
                    return Optional.of(fromRow(rows));
                }
                return Optional.empty();
            }
        }
    }

    // READ all
    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT * FROM customers ORDER BY customer_id";
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                customers.add(fromRow(rows));
            }
        }
        return customers;
    }

    // UPDATE
    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET first_name = ?, surname = ?, address = ? WHERE customer_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getSurname());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getCustomerId());
            statement.executeUpdate();
        }
    }

    // DELETE (a customer who still has accounts cannot be deleted: the foreign key blocks it)
    public void delete(String customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customerId);
            statement.executeUpdate();
        }
    }

    // Turns one database row into a Customer object (using the restore constructor)
    private Customer fromRow(ResultSet row) throws SQLException {
        return new Customer(
                row.getString("customer_id"),
                row.getString("first_name"),
                row.getString("surname"),
                row.getString("address"));
    }
}
