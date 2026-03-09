package it.salvatoregargano.weendtray.acl;

public class UserDeactivatedError extends CredentialsServiceError {
    public UserDeactivatedError(String message) {
        super(message);
    }

    public UserDeactivatedError() {
        super();
    }
}
