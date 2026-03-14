package it.garganovolpe.weendtray.logging;

import it.garganovolpe.weendtray.Main;

/**
 * {@link Logger} that prints messages to the console.
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

        String builder = '[' + Main.executionId + "] " + '[' + level + "]: " + message;
        System.out.println(builder);
    }
}
