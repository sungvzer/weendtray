package it.salvatoregargano.weendtray.acl;

public class CredentialsService {
    private static CredentialsService instance = null;
    private User loggedUser = null;

    public static CredentialsService getInstance() {
        if (instance == null) {
            instance = new CredentialsService();
        }
        return instance;
    }

    public void login(String username, String password) throws CredentialsServiceError {
        User user = UserPersistence.getUserByUsername(username);
        if (user == null || !user.verifyPassword(password)) {
            throw new BadCredentialsError();
        }

        if (!user.isActive()) {
            throw new UserDeactivatedError();
        }

        loggedUser = user;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void logout() {
        loggedUser = null;
    }
}
