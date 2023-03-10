package gameEngine;

import java.util.ArrayList;

public abstract class Subject {

    ArrayList<IObserver> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(int score, int highScore, int remainingLives, boolean flag) {
        for (IObserver observer : observers) {
            observer.update(score, highScore, remainingLives, flag);
        }
    }

}
