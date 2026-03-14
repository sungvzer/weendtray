package it.garganovolpe.weendtray.patterns;

/**
 * The Observer interface represents an object that can be notified of events or
 * changes in an Observable. It defines a single method, update, which is called
 * by the Observable when an event occurs. The Observer pattern allows for a
 * decoupled interaction between the observable object and its observers,
 * enabling multiple observers to react to changes in the observable without
 * tightly coupling them together. This pattern is commonly used in scenarios
 * such as event handling, where multiple components need to be informed of
 * changes or updates in the system without being directly dependent on each
 * other.
 */
public interface Observer<E> {
    void update(E event);
}
