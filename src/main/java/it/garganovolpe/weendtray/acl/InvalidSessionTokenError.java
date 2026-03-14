package it.garganovolpe.weendtray.acl;

/**
 * The InvalidSessionTokenError class represents an error that occurs when a
 * session token is invalid or has expired. This error is typically thrown when
 * a user
 * attempts to resume a session with an invalid token, indicating that the
 * session cannot be resumed due to authentication issues. It extends the
 * CredentialsServiceError class, providing a specific error message to indicate
 * the nature of the problem encountered during session resumption.
 */
public class InvalidSessionTokenError extends CredentialsServiceError {
    public InvalidSessionTokenError() {
        super("The session token is invalid or has expired.");
    }
}
