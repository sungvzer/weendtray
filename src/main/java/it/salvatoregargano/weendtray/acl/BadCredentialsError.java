package it.salvatoregargano.weendtray.acl;

/**
 * Indicates that the provided credentials are invalid, such as an incorrect
 * password or username.
 * This error is typically thrown when a user attempts to authenticate with
 * invalid credentials, and it serves as a signal that the authentication
 * process has failed due to incorrect input.
 */
public class BadCredentialsError extends CredentialsServiceError {
    public BadCredentialsError(String message) {
        super(message);
    }

    public BadCredentialsError() {
        super();
    }
}
