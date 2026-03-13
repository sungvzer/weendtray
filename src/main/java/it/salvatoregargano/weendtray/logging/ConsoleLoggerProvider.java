package it.salvatoregargano.weendtray.logging;

public class ConsoleLoggerProvider implements LoggerProvider {
    @Override
    public Logger createLogger() {
        return ConsoleLogger.getInstance();
    }
}
