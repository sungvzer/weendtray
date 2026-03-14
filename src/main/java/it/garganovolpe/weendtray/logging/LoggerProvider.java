package it.garganovolpe.weendtray.logging;
/* 
 * An interface for creating logger instances used as a factory.
 * @see ConsoleLoggerProvider
 * @see DatabaseLoggerProvider
 * @see CombinedLoggerProvider
 */
public interface LoggerProvider {
    Logger createLogger();
}
