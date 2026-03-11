package it.salvatoregargano.weendtray.acl;

/**
 * The CredentialsServiceError class serves as a base class for exceptions
 * related to user authentication and session management within the
 * CredentialsService. It provides a common structure for handling errors that
 * may occur during login, session resumption, and other credential-related
 * operations. Specific error types, such as {@link BadCredentialsError} and
 * {@link InvalidSessionTokenError}, can extend this class to provide more
 * detailed
 * information about the nature of the error encountered during authentication
 * processes.
 */
public abstract class CredentialsServiceError extends Error {
    public CredentialsServiceError(String message) {
        super(message);
    }

    public CredentialsServiceError() {
        super();
    }
}
