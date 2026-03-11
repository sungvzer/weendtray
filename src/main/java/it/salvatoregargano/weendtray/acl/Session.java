package it.salvatoregargano.weendtray.acl;

import java.util.Date;

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
