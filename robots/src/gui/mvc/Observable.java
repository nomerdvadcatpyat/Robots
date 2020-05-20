package gui.mvc;

import java.util.ArrayList;
import java.util.Collection;

public interface Observable {
    Collection<Observer> observers = new ArrayList<>();

    default void addObserver(Observer observer) {
        observers.add(observer);
    }

    default void notifyObservers() {
        observers.forEach(x -> x.update(this));
    }
}
