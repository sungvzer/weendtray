package it.salvatoregargano.weendtray.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton that ensures only one connection is used throughout an execution.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:telephoneManagement.db");
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlite:telephoneManagement.db");
        }
        return connection;
    }

    public boolean tableExists(String table) throws SQLException {
        final var meta = connection.getMetaData();
        final var tables = meta.getTables(null, null, table, null);
        return tables.next();
    }
}
