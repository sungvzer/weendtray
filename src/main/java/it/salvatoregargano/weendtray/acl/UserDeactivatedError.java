package it.salvatoregargano.weendtray.acl;

/**
 * The UserDeactivatedError class represents an error that occurs when a user
 * account has been deactivated. This error is typically thrown when a user
 * attempts to log in or perform actions that require an active account, but
 * their account has been marked as inactive.
 */
public class UserDeactivatedError extends CredentialsServiceError {
    public UserDeactivatedError(String message) {
        super(message);
    }

    public UserDeactivatedError() {
        super();
    }
}
