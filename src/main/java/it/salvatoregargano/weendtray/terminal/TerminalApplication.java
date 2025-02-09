package it.salvatoregargano.weendtray.terminal;

import it.salvatoregargano.weendtray.logging.CombinedLogger;

public class TerminalApplication {
    public static void run() {
        var logger = CombinedLogger.getInstance();
        logger.info("Starting terminal application");
    }
}
