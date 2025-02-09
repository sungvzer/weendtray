package it.salvatoregargano.weendtray.logging;

import it.salvatoregargano.weendtray.Entrypoint;

/**
 * PhoneEventLogger that logs messages to the console.
 */
public class ConsoleLogger extends Logger {
    private static ConsoleLogger instance;

    private ConsoleLogger() {
    }

    public static ConsoleLogger getInstance() {
        if (instance == null) {
            instance = new ConsoleLogger();
        }
        return instance;
    }

    @Override
    public final void log(String message, LogLevel level) {
        if (level.compareTo(logLevel) < 0) {
            return;
        }

        String builder = '[' + Entrypoint.executionId + "] " + '[' + level + "]: " + message;
        System.out.println(builder);
    }
}
