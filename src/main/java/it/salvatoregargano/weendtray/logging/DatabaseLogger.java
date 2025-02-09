package it.salvatoregargano.weendtray.logging;

import it.salvatoregargano.weendtray.Entrypoint;
import it.salvatoregargano.weendtray.persistence.DatabaseConnection;

import java.sql.SQLException;

/**
 * Logs messages to a database.
 *
 * @see Logger
 */
public class DatabaseLogger extends Logger {
    private static DatabaseLogger instance;

    private DatabaseLogger() {
    }

    /**
     * Singleton pattern.
     *
     * @return the instance of the DatabaseLogger
     */
    public static DatabaseLogger getInstance() {
        if (instance == null) {
            instance = new DatabaseLogger();
        }
        return instance;
    }

    @Override
    public final void log(String message, LogLevel level) {
        if (level.compareTo(logLevel) < 0) {
            return;
        }
        try {
            var connection = DatabaseConnection.getInstance().getConnection();
            try (var statement = connection.prepareStatement("INSERT INTO logs (message, level, execution_id) VALUES (?, ?, ?)")) {
                statement.setString(1, message);
                statement.setString(2, level.toString());
                statement.setString(3, Entrypoint.executionId);
                statement.execute();
            }

        } catch (SQLException e) {
            // At the moment we don't care if some messages do not get logged.
        }
    }
}
