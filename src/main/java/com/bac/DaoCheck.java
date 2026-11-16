package com.bac;

import com.bac.dao.CustomerDAO;
import com.bac.dao.DatabaseManager;
import com.bac.model.Customer;

import java.sql.SQLException;

public class DaoCheck {

    public static void main(String[] args) throws SQLException {
        DatabaseManager.initialiseSchema();
        CustomerDAO customerDAO = new CustomerDAO();

        Customer customer = new Customer("Test", "User", "Plot 1, Gaborone");
        String id = customer.getCustomerId();

        customerDAO.insert(customer);
        System.out.println("INSERT -> saved " + id);

        System.out.println("FIND   -> " + customerDAO.findById(id).orElseThrow().getFullName());

        customer.setAddress("Plot 99, Francistown");
        customerDAO.update(customer);
        System.out.println("UPDATE -> address is now: " + customerDAO.findById(id).orElseThrow().getAddress());

        System.out.println("LIST   -> " + customerDAO.findAll().size() + " customer(s) in the table");

        customerDAO.delete(id);
        System.out.println("DELETE -> still there? " + customerDAO.findById(id).isPresent());
    }
}
