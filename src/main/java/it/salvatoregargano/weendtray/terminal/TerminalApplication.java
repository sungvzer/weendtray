package it.salvatoregargano.weendtray.terminal;

import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.logging.ConsoleLogger;
import it.salvatoregargano.weendtray.logging.LogLevel;
import it.salvatoregargano.weendtray.terminal.screens.AdminUserScreen;
import it.salvatoregargano.weendtray.terminal.screens.HomeScreen;
import it.salvatoregargano.weendtray.terminal.screens.RegularUserScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalApplication {
    public void run(boolean debug) throws IOException {
        new HomeScreen(debug).show();
    }
}
