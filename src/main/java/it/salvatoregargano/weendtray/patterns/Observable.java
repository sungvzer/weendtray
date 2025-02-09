package it.salvatoregargano.weendtray.patterns;

import java.util.ArrayList;

// Observable Class
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