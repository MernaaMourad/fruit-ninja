package gameActions;

import gameObjects.gameObject.GameObject;

import java.util.ArrayList;

public class GameMemento {

    private int difficulty;
    private int lives;
    private int currentScore;
    private int highestScore;
    private ArrayList<GameObject> gameObjects;

    public GameMemento(int difficulty, int currentScore, int highestScore, int lives) {
        this.difficulty = difficulty;
        this.currentScore = currentScore;
        this.highestScore = highestScore;
        this.lives = lives;
    }


    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getLives() {
        return lives;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

}
