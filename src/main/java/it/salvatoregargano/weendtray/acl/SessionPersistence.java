package it.salvatoregargano.weendtray.acl;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.persistence.DatabaseConnection;

public class SessionPersistence {
    public static Optional<User> getUserBySessionToken(String token) {
        try (var statement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                "SELECT user_id FROM user_session WHERE session_token = ? AND expires_at > ?")) {

            statement.setString(1, token);
            statement.setString(2, new Date(System.currentTimeMillis()).toString());
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                return Optional.ofNullable(UserPersistence.getUserById(userId));
            }
        } catch (SQLException e) {
            CombinedLogger.getInstance().error("Error while retrieving user by session token: " + e.getMessage());
        }

        return Optional.empty();
    }

    public static void createSessionForUser(User user) {
        final CombinedLogger logger = CombinedLogger.getInstance();
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

    public static void deleteSessionsForUser(User user) {
        final CombinedLogger logger = CombinedLogger.getInstance();
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
