package it.salvatoregargano.weendtray.acl;

public class BadCredentialsError extends CredentialsServiceError {
    public BadCredentialsError(String message) {
        super(message);
    }

    public BadCredentialsError() {
        super();
    }
}
