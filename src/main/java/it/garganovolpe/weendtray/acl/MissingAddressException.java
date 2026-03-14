package it.garganovolpe.weendtray.acl;
/*
    * An exception thrown when a required address is missing during user persistence operations.
*/
public class MissingAddressException extends UserPersistenceException {
    public MissingAddressException(String message) {
        super(message);
    }

}
