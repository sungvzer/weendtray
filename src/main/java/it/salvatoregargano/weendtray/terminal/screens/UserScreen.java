package it.salvatoregargano.weendtray.terminal.screens;

import it.salvatoregargano.weendtray.acl.User;

public abstract class UserScreen<T> extends AScreen<T> {
    protected User user;

    public UserScreen(User user) {
        this.user = user;
    }
}
