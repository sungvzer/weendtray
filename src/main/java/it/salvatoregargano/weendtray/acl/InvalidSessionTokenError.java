package it.salvatoregargano.weendtray.acl;

public class InvalidSessionTokenError extends CredentialsServiceError {
    public InvalidSessionTokenError() {
        super("The session token is invalid or has expired.");
    }
}
