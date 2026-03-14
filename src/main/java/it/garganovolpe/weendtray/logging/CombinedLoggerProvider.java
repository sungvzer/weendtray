package it.garganovolpe.weendtray.logging;
/*
    * A concrete implementation of {@link LoggerProvider} that provides a singleton instance of {@link CombinedLogger}.
*/
public class CombinedLoggerProvider implements LoggerProvider {
    @Override
    public Logger createLogger() {
        return CombinedLogger.getInstance();
    }
}
