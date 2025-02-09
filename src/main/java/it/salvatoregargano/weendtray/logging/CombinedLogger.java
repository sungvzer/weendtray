package it.salvatoregargano.weendtray.logging;

/**
 * A logger that logs to both the console and the database.
 * <p>
 * Useful for debugging and auditing purposes.
 */
public class CombinedLogger extends Logger {
    private static CombinedLogger instance;

    private CombinedLogger() {
    }

    public static CombinedLogger getInstance() {
        if (instance == null) {
            instance = new CombinedLogger();
        }
        return instance;
    }
    
    @Override
    public final void setLogLevel(LogLevel level) {
        ConsoleLogger.getInstance().setLogLevel(level);
        DatabaseLogger.getInstance().setLogLevel(level);
    }

    @Override
    public final void log(String message, LogLevel level) {
        ConsoleLogger.getInstance().log(message, level);
        DatabaseLogger.getInstance().log(message, level);
    }
}
