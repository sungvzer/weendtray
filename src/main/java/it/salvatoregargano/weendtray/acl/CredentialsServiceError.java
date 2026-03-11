package it.salvatoregargano.weendtray.acl;

public abstract class CredentialsServiceError extends Error {
    public CredentialsServiceError(String message) {
        super(message);
    }

    public CredentialsServiceError() {
        super();
    }
}
