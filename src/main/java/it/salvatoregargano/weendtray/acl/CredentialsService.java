package it.salvatoregargano.weendtray.acl;

import java.util.Optional;

import it.salvatoregargano.weendtray.logging.CombinedLogger;

public class CredentialsService {
    private static CredentialsService instance = null;
    private User loggedUser = null;

    public static CredentialsService getInstance() {
        if (instance == null) {
            instance = new CredentialsService();
        }
        return instance;
    }

    public void resumeSession(User user, String token) throws CredentialsServiceError {
        Optional<User> sessionUser = SessionPersistence.getUserBySessionToken(token);
        if (sessionUser.isEmpty() || sessionUser.get().getId() != user.getId()) {
            throw new InvalidSessionTokenError();
        }

        CombinedLogger.getInstance()
                .info("User %s logged in with session token.".formatted(user.getUsername()));
        loggedUser = user;
    }

    public void login(String username, String password, boolean keepSession) throws CredentialsServiceError {
        User user = UserPersistence.getUserByUsername(username);
        if (user == null || !user.verifyPassword(password)) {
            throw new BadCredentialsError();
        }

        if (!user.isActive()) {
            throw new UserDeactivatedError();
        }

        if (keepSession) {
            SessionPersistence.createSessionForUser(user);
        }

        loggedUser = user;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void logout() {
        SessionPersistence.deleteSessionsForUser(loggedUser);
        loggedUser = null;
    }
}
