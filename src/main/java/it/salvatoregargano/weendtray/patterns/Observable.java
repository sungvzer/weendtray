package it.salvatoregargano.weendtray.patterns;

import java.util.ArrayList;

/**
 * The Observable class represents an object that can be observed by multiple
 * observers. It maintains a list of observers and provides methods to add,
 * remove, and notify them of events. The Observable class is a key component of
 * the Observer design pattern, which allows for a decoupled interaction between
 * the observable object and its observers. When an event occurs, the Observable
 * can notify all registered observers, allowing them to react accordingly. This
 * pattern is commonly used in scenarios such as event handling, where multiple
 * components need to be informed of changes or updates in the system without
 * tightly coupling them together.
 */
public class Observable<E> {
    private final ArrayList<Observer<E>> observers = new ArrayList<>();

    public void addObserver(Observer<E> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<E> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(E event) {
        for (var observer : observers) {
            observer.update(event);
        }
    }
}
