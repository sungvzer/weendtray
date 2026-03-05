package it.salvatoregargano.weendtray.acl;

public class CredentialsService {
    private static CredentialsService instance = null;

    public static CredentialsService getInstance() {
        if (instance == null) {
            instance = new CredentialsService();
        }
        return instance;
    }

    public boolean checkUserCredentials(String username, String password) {
        User user = UserPersistence.getUserByUsername(username);
        if (user == null) return false;

        return user.verifyPassword(password);

    }
}
