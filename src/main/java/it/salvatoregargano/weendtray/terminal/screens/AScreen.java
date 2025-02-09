package it.salvatoregargano.weendtray.terminal.screens;

import java.io.IOException;

public abstract class AScreen<T> {
    public abstract boolean show() throws IOException;

    protected abstract T parseCommand(String command);
}
