package it.salvatoregargano.weendtray.terminal;

import it.salvatoregargano.weendtray.terminal.screens.HomeScreen;

import java.io.IOException;
import java.sql.SQLException;

public class TerminalApplication {
    public void run(boolean debug) throws IOException, SQLException {
        new HomeScreen(debug).show();
    }
}
