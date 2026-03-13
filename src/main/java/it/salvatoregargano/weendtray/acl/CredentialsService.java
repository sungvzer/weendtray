package it.salvatoregargano.weendtray.acl;

import java.util.Optional;

import it.salvatoregargano.weendtray.logging.GetLoggerProviderFromEnv;
import it.salvatoregargano.weendtray.logging.LoggerInjector;
import it.salvatoregargano.weendtray.logging.LoggerProvider;

/**
 * The CredentialsService class is responsible for managing user authentication
 * and session management within the application. It provides methods for
 * logging in, resuming sessions, retrieving the currently logged-in user, and
 * logging out. The service interacts with user data and session persistence
 * layers to ensure secure and efficient handling of user credentials and
 * sessions.
 */
public class CredentialsService {
    private static CredentialsService instance = null;
    private User loggedUser = null;

    @GetLoggerProviderFromEnv(defaultType = "COMBINED")
    private LoggerProvider loggerProvider;

    public CredentialsService() {
        LoggerInjector.inject(this);
    }

    public static CredentialsService getInstance() {
        if (instance == null) {
            instance = new CredentialsService();
        }
        return instance;
    }

    /**
     * Resumes a user session based on the provided user and session token. It
     * checks if the session token is valid and corresponds to the given user. If
     * the token is valid, the user is logged in; otherwise, an
     * InvalidSessionTokenError is thrown.
     * 
     * @param user  The user for whom the session is being resumed.
     * @param token The session token used to identify the session to be resumed.
     * @throws CredentialsServiceError if the session token is invalid or does not
     *                                 correspond to the user.
     */
    public void resumeSession(User user, String token) throws CredentialsServiceError {
        Optional<User> sessionUser = SessionPersistence.getInstance().getUserBySessionToken(token);
        if (sessionUser.isEmpty() || sessionUser.get().getId() != user.getId()) {
            throw new InvalidSessionTokenError();
        }

        loggerProvider.createLogger()
                .info("User %s logged in with session token.".formatted(user.getUsername()));
        loggedUser = user;
    }

    /**
     * Logs in a user using the provided username and password. If the credentials
     * are valid and the user is active, the user is logged in. If the keepSession
     * flag is set to true, a new session is created for the user. If the
     * credentials are invalid or the user is deactivated, appropriate exceptions
     * are thrown.
     * 
     * @param username    The username of the user attempting to log in.
     * @param password    The password of the user attempting to log in.
     * @param keepSession A flag indicating whether to create a new session for the
     *                    user.
     * @throws CredentialsServiceError If the credentials are invalid or the user is
     *                                 deactivated.
     */
    public void login(String username, String password, boolean keepSession) throws CredentialsServiceError {
        User user = UserPersistence.getInstance().getUserByUsername(username);
        if (user == null || !user.verifyPassword(password)) {
            throw new BadCredentialsError();
        }

        if (!user.isActive()) {
            throw new UserDeactivatedError();
        }

        if (keepSession) {
            SessionPersistence.getInstance().createSessionForUser(user);
        }

        loggedUser = user;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    /**
     * Logs out the currently logged-in user. This method deletes all active
     * sessions for the user and clears the logged-in user reference.
     */
    public void logout() {
        SessionPersistence.getInstance().deleteSessionsForUser(loggedUser);
        loggedUser = null;
    }
}
