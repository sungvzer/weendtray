package it.garganovolpe.weendtray.logging;
/* 
 * A concrete implementation of {@link LoggerProvider} that provides a singleton instance of {@link DatabaseLogger}.
 */
public class DatabaseLoggerProvider implements LoggerProvider {
    @Override
    public Logger createLogger() {
        return DatabaseLogger.getInstance();
    }
}
