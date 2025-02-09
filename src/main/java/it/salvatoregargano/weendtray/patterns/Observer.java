package it.salvatoregargano.weendtray.patterns;

public interface Observer<E> {
    void update(E event);
}
