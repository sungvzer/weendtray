package it.salvatoregargano.weendtray.acl;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

import it.salvatoregargano.weendtray.logging.GetLoggerProviderFromEnv;
import it.salvatoregargano.weendtray.logging.Logger;
import it.salvatoregargano.weendtray.logging.LoggerInjector;
import it.salvatoregargano.weendtray.logging.LoggerProvider;
import it.salvatoregargano.weendtray.persistence.DatabaseConnection;

/**
 * <p>
 * The SessionPersistence class is responsible for managing user sessions within
 * the application. It provides methods to create, retrieve, and delete user
 * sessions, as well as to handle session tokens.
 * </p>
 * 
 * <p>
 * This class interacts with the
 * database to store and retrieve session information, ensuring that user
 * sessions are properly maintained and secured. It also handles the creation of
 * session files on the user's system to facilitate session resumption.
 * </p>
 */
public class SessionPersistence {
    @GetLoggerProviderFromEnv(defaultType = "COMBINED")
    private LoggerProvider loggerProvider;
    private static SessionPersistence instance = null;

    private SessionPersistence() {
        LoggerInjector.inject(this);
    }

    public static SessionPersistence getInstance() {
        if (instance == null) {
            instance = new SessionPersistence();
        }
        return instance;
    }

    /**
     * Retrieves a user associated with a given session token. This method checks
     * the
     * database for a valid session token and returns the corresponding user if the
     * token is valid and has not expired. If the token is invalid or has expired,
     * it returns an empty Optional.
     * 
     * @param token the session token to look up in the database
     * @return an Optional containing the User associated with the session token, or
     *         an empty Optional if the token is invalid or has expired
     */
    public Optional<User> getUserBySessionToken(String token) {
        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "SELECT user_id FROM user_session WHERE session_token = ? AND expires_at > ?")) {

            statement.setString(1, token);
            statement.setString(2, new Date(System.currentTimeMillis()).toString());
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                return Optional.ofNullable(UserPersistence.getInstance().getUserById(userId));
            }
        } catch (SQLException e) {
            loggerProvider.createLogger().error("Error while retrieving user by session token: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Creates a new session for the specified user.
     * 
     * @param user the user for whom to create a session
     */
    public void createSessionForUser(User user) {
        final Logger logger = loggerProvider.createLogger();
        logger.info("Creating session for user " + user.getUsername());

        if (user.getId() == -1) {
            logger.error("Cannot create session for user with id -1");
            return;
        }

        Session session = new Session(user);

        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "INSERT INTO user_session(session_token,user_id,expires_at) VALUES (?,?,?)")) {

            statement.setString(1, session.token);
            statement.setInt(2, session.user.getId());
            statement.setString(3, session.expiresAt.toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while creating user: " + e.getMessage());
        }

        File sessionFile = new File(System.getProperty("user.home"), ".weendtray_session");
        try {
            java.nio.file.Files.writeString(sessionFile.toPath(), user.getId() + ":" + session.token);
        } catch (java.io.IOException e) {
            logger.error("Error while writing session token to file: " + e.getMessage());
        }
    }

    /**
     * Deletes all sessions associated with the specified user. This method removes
     * all session records from the database for the given user and also deletes the
     * session token file from the user's system if it exists.
     * 
     * @param user the user for whom to delete sessions
     */
    public void deleteSessionsForUser(User user) {
        final Logger logger = loggerProvider.createLogger();
        logger.info("Deleting sessions for user " + user.getUsername());

        if (user.getId() == -1) {
            logger.error("Cannot delete sessions for user with id -1");
            return;
        }

        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "DELETE FROM user_session WHERE user_id = ?")) {

            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while deleting sessions for user: " + e.getMessage());
        }

        File sessionFile = new File(System.getProperty("user.home"), ".weendtray_session");
        if (sessionFile.exists()) {
            try {
                java.nio.file.Files.delete(sessionFile.toPath());
            } catch (java.io.IOException e) {
                logger.error("Error while deleting session token file: " + e.getMessage());
            }
        }
    }
}
