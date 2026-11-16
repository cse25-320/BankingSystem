package com.bac.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // File-based H2 database: the data survives when the program closes
    private static final String URL = "jdbc:h2:./data/bankdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Creates the tables from schema.sql if they don't exist yet
    public static void initialiseSchema() {
        try (InputStream in = DatabaseManager.class.getResourceAsStream("/schema.sql");
             Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            for (String command : sql.split(";")) {
                if (!command.isBlank()) {
                    statement.execute(command);
                }
            }
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Could not initialise the database", e);
        }
    }
}