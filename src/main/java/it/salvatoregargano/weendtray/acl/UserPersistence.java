package it.salvatoregargano.weendtray.acl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.salvatoregargano.weendtray.logging.GetLoggerProviderFromEnv;
import it.salvatoregargano.weendtray.logging.LoggerInjector;
import it.salvatoregargano.weendtray.logging.LoggerProvider;
import it.salvatoregargano.weendtray.persistence.DatabaseConnection;
import it.salvatoregargano.weendtray.telephone.billing.PhonePlan;
import it.salvatoregargano.weendtray.telephone.billing.UserAccountKind;

/**
 * Class to persist users to the database.
 * <p>
 * This class is responsible for saving users to the database. It uses the
 * {@link DatabaseConnection} class to connect to the database.
 * </p>
 *
 * <p>
 * The table `user` is created if it does not exist.
 * </p>
 *
 * @see User
 */
public class UserPersistence {
    @GetLoggerProviderFromEnv(defaultType = "COMBINED")
    private LoggerProvider loggerProvider;
    private static UserPersistence instance = null;

    private UserPersistence() {
        LoggerInjector.inject(this);
    }

    public static UserPersistence getInstance() {
        if (instance == null) {
            instance = new UserPersistence();
        }
        return instance;
    }

    /**
     * Gets a user by its id.
     * 
     * @param id the id of the user to get
     * @return the user with the specified id, or null if no user with the specified
     *         id exists
     */
    public User getUserById(int id) {
        var logger = loggerProvider.createLogger();
        if (id < 0) {
            return null;
        }

        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(
                        "SELECT `user`.*, user_address.address, user_address.city, user_address.postal_code, user_address.country, user_address.state FROM `user` LEFT JOIN user_address ON `user`.id = user_address.user_id WHERE `user`.`id` = ?")) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return fromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error while getting user by id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Creates a new user in the database.
     * <p>
     *
     * @param user The user to create.
     */
    private void createUser(User user) {
        var logger = loggerProvider.createLogger();

        if (user.getId() != -1) {
            return;
        }

        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "INSERT INTO `user` (`username`, `password`, `name`, `surname`, `role`, `phonenumber`, `plan`, `kind`) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING `id`")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getRole().toString());
            if (user.getRole() == UserRole.USER) {
                var regularUser = (RegularUser) user;
                statement.setString(6, regularUser.getPhoneNumber());
                statement.setString(7, regularUser.getPhonePlan().toString());
                statement.setString(8, regularUser.getKind().toString());
            } else {
                statement.setString(6, null);
                statement.setString(7, null);
                statement.setString(8, null);
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
     * Saves the address of a user to the database. This method is used to save the
     * address of a user to the database. It checks if the user is a regular user
     * and if it has an address. If the user is a regular user and has an address,
     * it saves the address to the database. If the user is not a regular user or
     * does not have an address, it does nothing.
     * 
     * @param user
     * @throws MissingAddressException if the user is a regular user and does not
     *                                 have an address
     */
    private void saveUserAddress(User user) throws MissingAddressException {
        var logger = loggerProvider.createLogger();

        if (user.getId() == -1) {
            return;
        }

        if (!(user instanceof RegularUser regularUser)) {
            return;
        }

        if (regularUser.getAddress() == null) {
            throw new MissingAddressException("The user is a regular user but does not have an address.");
        }

        boolean hasAddress = false;

        // Check if the user already has an address in the database
        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "SELECT COUNT(*) FROM `user_address` WHERE `user_id` = ?")) {
            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                hasAddress = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error while checking user address: " + e.getMessage());
        }

        if (!hasAddress) {
            insertUserAddress(regularUser);
        } else {
            updateUserAddress(regularUser);
        }
    }

    private void updateUserAddress(RegularUser regularUser) {
        var logger = loggerProvider.createLogger();
        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "UPDATE `user_address` SET `address` = ?, `city` = ?, `postal_code` = ?, `country` = ?, `state` = ? WHERE `user_id` = ?")) {
            statement.setString(1, regularUser.getAddress().getAddress());
            statement.setString(2, regularUser.getAddress().getCity());
            statement.setString(3, regularUser.getAddress().getPostalCode());
            statement.setString(4, regularUser.getAddress().getCountry());
            statement.setString(5, regularUser.getAddress().getState());
            statement.setInt(6, regularUser.getId());
            statement.execute();
        } catch (SQLException e) {
            logger.error("Error while updating user address: " + e.getMessage());

        }
    }

    private void insertUserAddress(RegularUser regularUser) {
        var logger = loggerProvider.createLogger();

        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "INSERT INTO `user_address` (`user_id`, `address`, `city`, `postal_code`, `country`, `state`) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, regularUser.getId());
            statement.setString(2, regularUser.getAddress().getAddress());
            statement.setString(3, regularUser.getAddress().getCity());
            statement.setString(4, regularUser.getAddress().getPostalCode());
            statement.setString(5, regularUser.getAddress().getCountry());
            statement.setString(6, regularUser.getAddress().getState());
            statement.execute();
        } catch (SQLException e) {
            logger.error("Error while saving user address: " + e.getMessage());

        }
    }

    /**
     * Updates a user in the database.
     * <p>
     * The user is updated based on the id.
     *
     * @param user The user to update.
     */
    private void updateUserById(User user) {
        var logger = loggerProvider.createLogger();

        if (user.getId() == -1) {
            return;
        }

        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "UPDATE `user` SET `username` = ?, `password` = ?, `name` = ?, `surname` = ?, `role` = ?, `plan` = ?, `phonenumber` = ?, `active` = ?, `kind` = ? WHERE `user`.`id` = ?")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getRole().toString());
            if (user instanceof RegularUser regularUser) {
                statement.setString(6, regularUser.getPhonePlan().toString());
                statement.setString(7, regularUser.getPhoneNumber());
                statement.setString(9, regularUser.getKind().toString());
            } else {
                statement.setString(6, "REGULAR");
                statement.setString(7, null);
                statement.setString(9, null);
            }
            statement.setBoolean(8, user.isActive());
            statement.setInt(10, user.getId());

            statement.execute();
        } catch (SQLException e) {
            logger.error("Error while updating user: " + e.getMessage());
        }
    }

    /**
     * Search for a user by its phone number.
     */
    public RegularUser getUserByPhoneNumber(String userPhoneNumber) {
        var logger = loggerProvider.createLogger();
        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(
                        "SELECT `user`.*, user_address.address, user_address.city, user_address.postal_code, user_address.country, user_address.state FROM `user` LEFT JOIN user_address ON `user`.id = user_address.user_id WHERE `phonenumber` = ?")) {
            statement.setString(1, userPhoneNumber);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return (RegularUser) fromResultSet(rs);
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
    public void saveUser(User user) {
        var logger = loggerProvider.createLogger();
        try {
            DatabaseConnection.getInstance().getConnection().setAutoCommit(false);

            if (user.getId() == -1) {
                createUser(user);
            } else {
                updateUserById(user);
            }

            saveUserAddress(user);

            DatabaseConnection.getInstance().getConnection().commit();
            logger.info("User saved: " + user.getId());
        } catch (SQLException e) {
            logger.error("Error while saving user: " + e.getMessage());
            try {
                DatabaseConnection.getInstance().getConnection().rollback();
            } catch (SQLException ex) {
                logger.error("Error while rolling back transaction: " + ex.getMessage());
            }
        } catch (MissingAddressException e) {
            logger.error("Error while saving user: " + e.getMessage());
            try {
                DatabaseConnection.getInstance().getConnection().rollback();
            } catch (SQLException ex) {
                logger.error("Error while rolling back transaction: " + ex.getMessage());
            }
        } finally {
            try {
                DatabaseConnection.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error while setting auto commit to true: " + e.getMessage());
            }
        }
    }

    /**
     * Checks if at least one admin user exists in the database.
     *
     * @return true if at least one admin user exists, false otherwise.
     */
    public boolean atLeastOneAdminUser() {
        var logger = loggerProvider.createLogger();
        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(*) FROM `user` WHERE `role` = ?")) {
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

    /**
     * Promotes a user to admin. This method updates the user's role to ADMIN in the
     * database. If the user is already an admin, this method does nothing.
     * 
     * @param user the user to promote to admin
     */
    public void promoteUser(User user) {
        var logger = loggerProvider.createLogger();

        if (user.getId() == -1) {
            return;
        }

        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement("UPDATE `user` SET `role` = ? WHERE `user`.`id` = ?")) {
            statement.setString(1, UserRole.ADMIN.toString());
            statement.setInt(2, user.getId());
            statement.execute();

        } catch (SQLException e) {
            logger.error("Error while promoting user: " + e.getMessage());
        }

        logger.info("Promoted user: " + user.getId() + " (" + user.getUsername() + ")");
    }

    /**
     * Lists all users in the database.
     * 
     * @return an ArrayList of all users in the database.
     */
    public ArrayList<User> listUsers() {
        ArrayList<User> users = new ArrayList<>();

        var logger = loggerProvider.createLogger();
        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(
                        "SELECT `user`.*, user_address.address, user_address.city, user_address.postal_code, user_address.country, user_address.state FROM `user` LEFT JOIN user_address ON `user`.id = user_address.user_id")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User u = fromResultSet(rs);
                users.add(u);
            }
        } catch (SQLException e) {
            logger.error("Error while listing users: " + e.getMessage());
        }
        return users;

    }

    /**
     * Checks if a phone number is already in use by another user. This method
     * queries the database to determine if any user has the specified phone number.
     * It returns true if the phone number is already associated with an existing
     * user, and false otherwise.
     * 
     * @param phoneNumber the phone number to check for uniqueness
     * @return true if the phone number is already in use by another user, false
     *         otherwise
     */
    public boolean isPhoneNumberInUse(String phoneNumber) {
        var logger = loggerProvider.createLogger();
        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(*) FROM `user` WHERE `phonenumber` = ?")) {
            statement.setString(1, phoneNumber);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error while checking if phone number is in use: " + e.getMessage());
        }
        return false;
    }

    /**
     * Gets a user by its username. This method queries the database for a user with
     * the specified username and returns the corresponding User object if found. If
     * no user with the given username exists, it returns null.
     * 
     * @param username the username of the user to retrieve
     */
    public User getUserByUsername(String username) {
        var logger = loggerProvider.createLogger();
        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(
                        "SELECT `user`.*, user_address.address, user_address.city, user_address.postal_code, user_address.country, user_address.state FROM `user` LEFT JOIN user_address ON `user`.id = user_address.user_id WHERE `username` = ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return fromResultSet(rs);
            }
        } catch (

        SQLException e) {
            logger.error("Error while getting user by username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Toggles the active status of a user. This method updates the user's active
     * status in the database, switching it from active to inactive or vice versa.
     * If the user is currently active, it will be deactivated, and if it is
     * currently inactive, it will be activated. This method is typically used to
     * enable or disable user accounts without permanently deleting them from the
     * database.
     */
    public void toggleUser(User user) {
        var logger = loggerProvider.createLogger();

        if (user.getId() == -1) {
            return;
        }

        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement("UPDATE `user` SET `active` = ? WHERE `user`.`id` = ?")) {
            statement.setInt(1, user.isActive() ? 0 : 1);
            statement.setInt(2, user.getId());
            statement.execute();

        } catch (SQLException e) {
            logger.error("Error while deactivating user: " + e.getMessage());
        }

        logger.info("User active status toggled: " + user.getId() + " (" + user.getUsername() + "), now "
                + (user.isActive() ? "inactive" : "active"));

    }

    public void changeUserPlan(User user, PhonePlan phonePlan) {
        var logger = loggerProvider.createLogger();

        if (user.getId() == -1) {
            return;
        }

        try (var statement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement("UPDATE `user` SET `plan` = ? WHERE `user`.`id` = ?")) {
            statement.setString(1, phonePlan.toString());
            statement.setInt(2, user.getId());
            statement.execute();

        } catch (SQLException e) {
            logger.error("Error while changing user plan: " + e.getMessage());
        }

        logger.info(
                "User plan changed: " + user.getId() + " (" + user.getUsername() + "), now " + phonePlan.toString());
    }

    private User fromResultSet(ResultSet rs) throws SQLException {
        var role = UserRole.valueOf(rs.getString("role"));
        if (role == UserRole.USER) {
            return new UserBuilder().withId(rs.getInt("id"))
                    .withUsername(rs.getString("username"))
                    .withHashedPassword(rs.getString("password"))
                    .withName(rs.getString("name"))
                    .withSurname(rs.getString("surname"))
                    .withRole(UserRole.USER)
                    .withActive(rs.getInt("active") == 1)
                    .withPhonePlan(PhonePlan.valueOf(rs.getString("plan")) == null ? PhonePlan.REGULAR
                            : PhonePlan.valueOf(rs.getString("plan")))
                    .withPhoneNumber(rs.getString("phonenumber"))
                    .withAddress(new UserAddress(
                            rs.getString("address"),
                            rs.getString("city"),
                            rs.getString("postal_code"),
                            rs.getString("country"),
                            rs.getString("state")))
                    .withKind(UserAccountKind.of(rs.getString("kind")))
                    .build();

        }

        return new UserBuilder().withId(rs.getInt("id"))
                .withUsername(rs.getString("username"))
                .withHashedPassword(rs.getString("password"))
                .withName(rs.getString("name"))
                .withSurname(rs.getString("surname"))
                .withRole(UserRole.ADMIN)
                .withActive(rs.getInt("active") == 1)
                .build();
    }
}
