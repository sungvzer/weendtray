package it.salvatoregargano.weendtray.terminal.screens;

import java.io.IOException;
import java.sql.SQLException;

public abstract class AScreen<T> {
    public abstract boolean show() throws IOException, SQLException;

    protected abstract T parseCommand(String command);
}
