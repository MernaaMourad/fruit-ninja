package gameEngine;

public interface IObserver {
    void update(int score, int highScore, int remainingLives, boolean flag);
}