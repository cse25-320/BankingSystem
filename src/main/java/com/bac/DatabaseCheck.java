package com.bac;

import com.bac.dao.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseCheck {

    public static void main(String[] args) throws SQLException {
        DatabaseManager.initialiseSchema();

        try (Connection connection = DatabaseManager.getConnection()) {
            System.out.println("Connected to: " + connection.getMetaData().getDatabaseProductName());

            ResultSet tables = connection.getMetaData()
                    .getTables(null, "PUBLIC", "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println("Table found: " + tables.getString("TABLE_NAME"));
            }
        }
    }
}
