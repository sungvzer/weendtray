package it.salvatoregargano.weendtray.logging;

/**
 * Interface for logging messages.
 *
 * @see ConsoleLogger
 * @see DatabaseLogger
 * @see CombinedLogger
 */
public abstract class Logger {
    protected LogLevel logLevel = LogLevel.DEBUG;

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * Logs a message with the given log level.
     *
     * @param message The message to log.
     * @param level   The log level of the message.
     * @see LogLevel
     */
    public abstract void log(String message, LogLevel level);

    /**
     * Logs an info message.
     *
     * @param message The message to log.
     * @see LogLevel#INFO
     */
    public void info(String message) {
        log(message, LogLevel.INFO);
    }

    /**
     * Logs a warning message.
     *
     * @param message The message to log.
     * @see LogLevel#WARNING
     */
    public void warn(String message) {
        log(message, LogLevel.WARNING);
    }

    /**
     * Logs an error message.
     *
     * @param message The message to log.
     * @see LogLevel#ERROR
     */
    public void error(String message) {
        log(message, LogLevel.ERROR);
    }

    /**
     * Logs a debug message.
     *
     * @param message The message to log.
     * @see LogLevel#DEBUG
     */
    public void debug(String message) {
        log(message, LogLevel.DEBUG);
    }
}
