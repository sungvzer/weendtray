package it.garganovolpe.weendtray.logging;
/*
 * A concrete implementation of {@link LoggerProvider} that provides a singleton instance of {@link ConsoleLogger}.
*/
public class ConsoleLoggerProvider implements LoggerProvider {
    @Override
    public Logger createLogger() {
        return ConsoleLogger.getInstance();
    }
}
