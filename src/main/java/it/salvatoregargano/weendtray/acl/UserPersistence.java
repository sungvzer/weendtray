package it.salvatoregargano.weendtray.acl;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.persistence.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to persist users to the database.
 * <p>
 * This class is responsible for saving users to the database. It uses the {@link DatabaseConnection} class to connect to the database.
 *
 * <p>
 * The table `user` is created if it does not exist.
 *
 * @see User
 */
public class UserPersistence {
    private static boolean ensureTableExists() {
        var logger = CombinedLogger.getInstance();
        try {
            var db = DatabaseConnection.getInstance();
            var connection = db.getConnection();
            if (!db.tableExists("user")) {
                logger.info("Creating user table");
            }
            var statement = connection.createStatement();
            // create table user with SQLite syntax
            statement.execute("CREATE TABLE IF NOT EXISTS `user` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `username` TEXT NOT NULL, `password` TEXT NOT NULL, `email` TEXT NOT NULL, `name` TEXT NOT NULL, `surname` TEXT NOT NULL, `role` TEXT NOT NULL)");

        } catch (SQLException e) {
            logger.error("Error while ensuring table `user` exists: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Creates a new user in the database.
     * <p>
     *
     * @param user The user to create.
     */
    private static void createUser(User user) {
        var logger = CombinedLogger.getInstance();
        if (!ensureTableExists()) {
            logger.error("Error while creating user: table does not exist");
            return;
        }

        if (user.getId() != -1) {
            return;
        }

        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("INSERT INTO `user` (`username`, `password`, `email`, `name`, `surname`, `role`) VALUES (?, ?, ?, ?, ?, ?) RETURNING `id`")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getName());
            statement.setString(5, user.getSurname());
            statement.setString(6, user.getRole().toString());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            logger.error("Error while creating user: " + e.getMessage());
        }
    }

    /**
     * Updates a user in the database.
     * <p>
     * The user is updated based on the id.
     *
     * @param user The user to update.
     */
    private static void updateUserById(User user) {
        var logger = CombinedLogger.getInstance();
        if (!ensureTableExists()) {
            logger.error("Error while updating user: table does not exist");
            return;
        }

        if (user.getId() == -1) {
            return;
        }

        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("UPDATE `user` SET `username` = ?, `password` = ?, `email` = ?, `name` = ?, `surname` = ?, `role` = ? WHERE `id` = ?")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getName());
            statement.setString(5, user.getRole().toString());
            statement.setInt(6, user.getId());
            statement.execute();
        } catch (SQLException e) {
            logger.error("Error while updating user: " + e.getMessage());
        }
    }

    /**
     * Saves a user to the database.
     *
     * @param user The user to save.
     */
    public static void saveUser(User user) {
        var logger = CombinedLogger.getInstance();

        if (user.getId() == -1) {
            createUser(user);
        } else {
            updateUserById(user);
        }

        logger.info("User saved: " + user.getId());
    }
}
