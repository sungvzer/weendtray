package it.garganovolpe.weendtray.acl;

public class MissingAddressException extends UserPersistenceException {
    public MissingAddressException(String message) {
        super(message);
    }

}
