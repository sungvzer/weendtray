package it.salvatoregargano.weendtray.terminal;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.logging.ConsoleLogger;
import it.salvatoregargano.weendtray.logging.LogLevel;

public class TerminalApplication {
    public static void run(boolean debug) {
        var logger = CombinedLogger.getInstance();

        if (debug) {
            CombinedLogger.getInstance().setLogLevel(LogLevel.DEBUG);
        } else {
            CombinedLogger.getInstance().setLogLevel(LogLevel.INFO);
        }

        boolean running = true;
        while (running) {
            logger.debug("Starting terminal application. Turning off terminal logging.");
            ConsoleLogger.getInstance().setLogLevel(LogLevel.OFF);
            logger.debug("Terminal logging turned off.");

            running = false;
        }
    }
}
