package it.garganovolpe.weendtray.logging;

public class CombinedLoggerProvider implements LoggerProvider {
    @Override
    public Logger createLogger() {
        return CombinedLogger.getInstance();
    }
}
