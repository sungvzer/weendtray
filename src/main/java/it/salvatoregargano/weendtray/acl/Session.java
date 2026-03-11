package it.salvatoregargano.weendtray.acl;

import java.util.Date;

/**
 * The Session class represents a user session within the application. It holds
 * information about the session, including the session token, the associated
 * user,
 * and the expiration date.
 */
public class Session {
    public String token;
    public User user;
    public Date expiresAt;

    public Session(User user) {
        this.token = java.util.UUID.randomUUID().toString();
        this.user = user;
        this.expiresAt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // expires in 24 hours
    }
}
