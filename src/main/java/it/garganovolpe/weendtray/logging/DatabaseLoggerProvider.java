package it.garganovolpe.weendtray.logging;

public class DatabaseLoggerProvider implements LoggerProvider {
    @Override
    public Logger createLogger() {
        return DatabaseLogger.getInstance();
    }
}
