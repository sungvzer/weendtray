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
    private static boolean tableDoesNotExist() {
        var logger = CombinedLogger.getInstance();
        try {
            var db = DatabaseConnection.getInstance();
            var connection = db.getConnection();
            if (!db.tableExists("user")) {
                logger.info("Creating user table");
            }
            var statement = connection.createStatement();
            // create table user with SQLite syntax
            statement.execute("CREATE TABLE IF NOT EXISTS `user` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `username` TEXT NOT NULL, `password` TEXT NOT NULL, `name` TEXT NOT NULL, `surname` TEXT NOT NULL, `role` TEXT NOT NULL, `plan` TEXT DEFAULT 'REGULAR', `phonenumber` TEXT DEFAULT NULL, `active` INTEGER DEFAULT 1)");
            statement.execute("CREATE UNIQUE INDEX IF NOT EXISTS `user_username` ON `user` (`username`)");
            statement.execute("CREATE UNIQUE INDEX IF NOT EXISTS `user_phonenumber` ON `user` (`phonenumber`)");
        } catch (SQLException e) {
            logger.error("Error while ensuring table `user` exists: " + e.getMessage());
            return true;
        }
        return false;
    }

    /**
     * Creates a new user in the database.
     * <p>
     *
     * @param user The user to create.
     */
    private static void createUser(User user) {
        var logger = CombinedLogger.getInstance();
        if (tableDoesNotExist()) {
            logger.error("Error while creating user: table does not exist");
            return;
        }

        if (user.getId() != -1) {
            return;
        }

        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("INSERT INTO `user` (`username`, `password`, `name`, `surname`, `role`, `phonenumber`) VALUES (?, ?, ?, ?, ?, ?) RETURNING `id`")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getRole().toString());
            if (user.getRole() == UserRole.USER) {
                var regularUser = (RegularUser) user;
                statement.setString(6, regularUser.getPhoneNumber());
            } else {
                statement.setString(6, null);
            }
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
        if (tableDoesNotExist()) {
            logger.error("Error while updating user: table does not exist");
            return;
        }

        if (user.getId() == -1) {
            return;
        }

        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("UPDATE `user` SET `username` = ?, `password` = ?, `name` = ?, `surname` = ?, `role` = ?, `plan` = ?, `phonenumber` = ? WHERE `id` = ?")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getRole().toString());
            if (user instanceof RegularUser regularUser) {
                statement.setString(6, regularUser.getPhonePlan().toString());
                statement.setString(7, regularUser.getPhoneNumber());
            } else {
                statement.setString(6, "REGULAR");
                statement.setString(7, null);
            }
            statement.setInt(8, user.getId());
            statement.execute();
        } catch (SQLException e) {
            logger.error("Error while updating user: " + e.getMessage());
        }
    }

    /**
     * Search for a user by its phone number.
     */
    public static RegularUser getUserByPhoneNumber(String userPhoneNumber) {
        var logger = CombinedLogger.getInstance();
        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("SELECT * FROM `user` WHERE `phonenumber` = ?")) {
            statement.setString(1, userPhoneNumber);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new RegularUser(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        PhonePlan.valueOf(rs.getString("plan")),
                        rs.getString("phonenumber"),
                        rs.getInt("active") == 1
                );
            }
        } catch (SQLException e) {
            logger.error("Error while getting user by phone number: " + e.getMessage());
        }
        return null;

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

    /**
     * Checks if at least one admin user exists in the database.
     *
     * @return true if at least one admin user exists, false otherwise.
     */
    public static boolean atLeastOneAdminUser() {
        var logger = CombinedLogger.getInstance();
        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("SELECT COUNT(*) FROM `user` WHERE `role` = ?")) {
            statement.setString(1, UserRole.ADMIN.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error while checking if admin user exists: " + e.getMessage());
        }
        return false;
    }

    public static void promoteUser(User user) {
        var logger = CombinedLogger.getInstance();
        if (tableDoesNotExist()) {
            logger.error("Error while promoting user: table does not exist");
            return;
        }

        if (user.getId() == -1) {
            return;
        }

        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("UPDATE `user` SET `role` = ? WHERE `id` = ?")) {
            statement.setString(1, UserRole.ADMIN.toString());
            statement.setInt(2, user.getId());
            statement.execute();

        } catch (SQLException e) {
            logger.error("Error while promoting user: " + e.getMessage());
        }

        logger.info("Promoted user: " + user.getId() + " (" + user.getUsername() + ")");
    }

    public static User getUserByUsername(String username) {
        var logger = CombinedLogger.getInstance();
        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("SELECT * FROM `user` WHERE `username` = ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                var role = UserRole.valueOf(rs.getString("role"));
                if (role == UserRole.USER) {
                    return new RegularUser(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            PhonePlan.valueOf(rs.getString("plan")),
                            rs.getString("phonenumber"),
                            rs.getInt("active") == 1
                    );
                }

                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        role,
                        rs.getInt("active") == 1
                );
            }
        } catch (SQLException e) {
            logger.error("Error while getting user by username: " + e.getMessage());
        }
        return null;
    }

    public static void toggleUser(User user) {
        var logger = CombinedLogger.getInstance();
        if (tableDoesNotExist()) {
            logger.error("Error while deactivating user: table does not exist");
            return;
        }

        if (user.getId() == -1) {
            return;
        }

        try (var statement = DatabaseConnection.
                getInstance().
                getConnection().
                prepareStatement("UPDATE `user` SET `active` = ? WHERE `id` = ?")) {
            statement.setInt(1, user.isActive() ? 0 : 1);
            statement.setInt(2, user.getId());
            statement.execute();

        } catch (SQLException e) {
            logger.error("Error while deactivating user: " + e.getMessage());
        }

        logger.info("User active status toggled: " + user.getId() + " (" + user.getUsername() + "), now " + (user.isActive() ? "inactive" : "active"));

    }
}
