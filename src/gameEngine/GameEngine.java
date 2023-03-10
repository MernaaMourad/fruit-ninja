package gameEngine;

import gameActions.GameAction;
import gameObjects.fruits.Images;
import gameObjects.fruits.Orange;
import gameObjects.fruits.Watermelon;
import gameObjects.gameObject.ENUM;
import gameObjects.gameObject.GameObject;
import gameObjects.bombs.Bomb;
import gameObjects.bombs.FatalBomb;
import gameObjects.commands.*;
import gameObjects.fruits.Apple;
import gui.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.util.Duration;

public class GameEngine extends Subject {

    private GamePlayWindow gamePlayWindow;
    private GameAction gameActions;

    private BonusCommand bonusCommand;
    private DecrementCommand decrementCommand;
    private IncrementCommand incrementCommand;
    private EndLifeCommand endLifeCommand;

    private int rand;
    private int temp;
    private boolean loaded;
    private int tempHighScore;
    private int tempCurrentScore;
    private long bonusTime;

    public void setGamePlayWindow(GamePlayWindow gamePlayWindow, int temp) {
        this.gamePlayWindow = gamePlayWindow;
        this.temp = temp;
    }

    public void setGameActions() {

        gameActions = GameAction.getInstance();
        int num = 0;
        File fp = new File("data.xml");
        if (fp.length() > 60) {
            num = gameActions.getGameMemento().getHighestScore();
        }

        if (temp == 1) {
            tempHighScore = gameActions.getGameMemento().getHighestScore();
            tempCurrentScore = gameActions.getGameMemento().getCurrentScore();
            gamePlayWindow.setGameObjects(gameActions.getGameMemento().getGameObjects());
            loaded = true;
            rand = gameActions.getGameMemento().getGameObjects().size();
        } else {
            loaded = false;
            if (fp.length() > 60) {
                tempHighScore = gameActions.getGameMemento().getHighestScore();
            }
            gameActions.setGameMemento(1, 0, num, 3);
            ArrayList<GameObject> gameobjects = new ArrayList<>();
            gameActions.getGameMemento().setGameObjects(gameobjects);
        }
        notifyObservers(getGameActions().getGameMemento().getCurrentScore(), getGameActions().getGameMemento().getHighestScore(), getGameActions().getGameMemento().getLives(), false);
        incrementCommand = new IncrementCommand(gameActions);
        decrementCommand = new DecrementCommand(gameActions);
        bonusCommand = new BonusCommand(gameActions);
        endLifeCommand = new EndLifeCommand(gameActions);

    }

    public void sliceEngine(ArrayList<GameObject> gameObjects, MouseEvent event, Timeline time) throws InterruptedException, IOException {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.isSliced() == null && (event.getX() >= gameObject.getImageView().getLayoutX()) && (event.getX() <= gameObject.getImageView().getLayoutX() + gameObject.getImageView().getFitWidth()) && (event.getY() >= gameObject.getImageView().getLayoutY()) && (event.getY() <= gameObject.getImageView().getLayoutY() + gameObject.getImageView().getFitHeight())) {
                gameObject.slice();
                if (gameObject.getObjectType() == ENUM.DangerousBomb) {
                    Image image = gameObject.getSlicedImage();
                    showSlicedImage(gameObject, image, 65, 65);
                    gameObject.setCommand(decrementCommand);
                    gameObject.swipe();
                    gamePlayWindow.getPane().getChildren().remove(gameObject.getImageView());
                    notifyObservers(getGameActions().getGameMemento().getCurrentScore(), getGameActions().getGameMemento().getHighestScore(), getGameActions().getGameMemento().getLives(), false);
                } else if (gameObject.getObjectType() == ENUM.FatalBomb) {
                    gameObject.setCommand(endLifeCommand);
                    gameObject.swipe();
                } else if (gameObject.getObjectType() == ENUM.DoubleScoreBanana) {
                    bonusTime = System.currentTimeMillis();
                    gamePlayWindow.getPane().getChildren().remove(gameObject.getImageView());
                    Image image = gameObject.getSlicedImage();
                    showSlicedImage(gameObject, image, 65, 65);
                    SoundTrack sliceSound = new SoundTrack();
                    sliceSound.playSliceSound();
                } else if (gameObject.getObjectType() == ENUM.MagicBean) {
                    gameObject.setCommand(bonusCommand);
                    gameObject.swipe();

                    gamePlayWindow.getPane().getChildren().remove(gameObject.getImageView());
                    SoundTrack sliceSound = new SoundTrack();
                    sliceSound.playSliceSound();

                    notifyObservers(getGameActions().getGameMemento().getCurrentScore(), getGameActions().getGameMemento().getHighestScore(), getGameActions().getGameMemento().getLives(), false);
                    notifyObservers(getGameActions().getGameMemento().getCurrentScore(), getGameActions().getGameMemento().getHighestScore(), getGameActions().getGameMemento().getLives(), true);

                } else {
                    gamePlayWindow.setComboCount();
                    SoundTrack sliceSound = new SoundTrack();
                    sliceSound.playSliceSound();

                    gameObject.setImageView(gameObject.getSlicedImage());
                    GameObject slicedGameObject;
                    if (gameObject.getObjectType().equals(ENUM.Apple)) {
                        slicedGameObject = new Apple();
                    } else if (gameObject.getObjectType().equals(ENUM.Orange)) {
                        slicedGameObject = new Orange();
                    } else {
                        slicedGameObject = new Watermelon();
                    }
                    slicedGameObject.setImageView(gameObject.getSlicedImage());
                    slicedGameObject.getImageView().setLayoutX(gameObject.getImageView().getLayoutX() + 100);
                    slicedGameObject.getImageView().setLayoutY(gameObject.getImageView().getLayoutY());
                    slicedGameObject.setFallingVelocity();
                    gamePlayWindow.getPane().getChildren().add(slicedGameObject.getImageView());
                    gameObject.setFallingVelocity(slicedGameObject.getFallingVelocity());
                    gameObject.setSliced(true);
                    slicedGameObject.setSliced(true);

                    RotateTransition rotateTransition = gamePlayWindow.setRotation(slicedGameObject.getImageView());
                    gamePlayWindow.getRotateTransitions().add(rotateTransition);
                    moveEngine(slicedGameObject, time);
                    gameObject.setCommand(incrementCommand);
                    if (gamePlayWindow.getComboCount() >= 3) {
                        gameObject.swipe();
                        BufferedImage img[]= Images.getBufferedImage();
                        Image image = SwingFXUtils.toFXImage(img[21], null);
                        showSlicedImage(gameObject, image, 85, 85);
                    }
                    if (System.currentTimeMillis() < bonusTime + 13000) {
                        gameObject.swipe();
                    }
                    gameObject.swipe();
                    notifyObservers(getGameActions().getGameMemento().getCurrentScore(), getGameActions().getGameMemento().getHighestScore(), getGameActions().getGameMemento().getLives(), true);
                    if (gameActions.getGameMemento().getHighestScore() < gameActions.getGameMemento().getCurrentScore()) {
                        gameActions.getGameMemento().setHighestScore(gameActions.getGameMemento().getCurrentScore());
                        gamePlayWindow.sethScore(gameActions.getGameMemento().getCurrentScore());
                        gamePlayWindow.saveGui();
                    }
                }
                break;
            }
        }
    }

    private void setDifficulty() {
        if (gameActions.getGameMemento().getCurrentScore() < 15) {
            gameActions.getGameMemento().setDifficulty(1);
            gamePlayWindow.getLabelDifficultyLevel().setText("Difficulty Level: 1");
        } else if (gameActions.getGameMemento().getCurrentScore() >= 15 && gameActions.getGameMemento().getCurrentScore() < 25) {
            gameActions.getGameMemento().setDifficulty(2);
            gamePlayWindow.getLabelDifficultyLevel().setText("Difficulty Level: 2");
        } else if (gameActions.getGameMemento().getCurrentScore() >= 25) {
            gameActions.getGameMemento().setDifficulty(3);
            gamePlayWindow.getLabelDifficultyLevel().setText("Difficulty Level: 3");
        }
    }

    public void moveEngine(GameObject gameObject, Timeline time) {
        Duration duration = Duration.millis(2000);
        RotateTransition rotateTransition = new RotateTransition(duration, gameObject.getImageView());
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setByAngle(360);
        gamePlayWindow.getRotateTransitions().add(rotateTransition);
        Timeline timeline = new Timeline();
        gamePlayWindow.getTimelines().add(timeline);
        setDifficulty();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(16), t -> {
            setDifficulty();
            setGameOver();
            if (gamePlayWindow.getGameObjects().size()==1 && gameObject.getObjectType().equals(ENUM.DangerousBomb) && gameObject.isSliced() != null) {
                generateNextFruits(time);
                gamePlayWindow.getGameObjects().remove(gameObject);
                setGameOver();
                timeline.stop();
            }
            if (gamePlayWindow.isPaused() == false && gameObject.getYLocation() <= 550) {
                rotateTransition.play();
                gameObject.move();
            } else {
                rotateTransition.pause();
                timeline.pause();
            }
            if (gameObject.getYLocation() >= 500) {
                gamePlayWindow.getGameObjects().remove(gameObject);
                gamePlayWindow.getTimelines().remove(timeline);
                gamePlayWindow.getRotateTransitions().remove(rotateTransition);
                if (gameObject.isSliced() == null) {
                    gameObject.setSliced(false);
                    if (!(gameObject.getObjectType().equals(ENUM.DangerousBomb)) && !(gameObject.getObjectType().equals(ENUM.FatalBomb)) && !(gameObject.getObjectType().equals(ENUM.DoubleScoreBanana)) && !(gameObject.getObjectType().equals(ENUM.MagicBean))) {
                        gameObject.setCommand(decrementCommand);
                        gameObject.swipe();
                        notifyObservers(getGameActions().getGameMemento().getCurrentScore(), getGameActions().getGameMemento().getHighestScore(), getGameActions().getGameMemento().getLives(), false);
                    }
                }
                gameObject.setDropOff(true);
                gamePlayWindow.getPane().getChildren().remove(gameObject.getImageView());

                if (gamePlayWindow.getGameObjects().size()==0 && gameActions.getGameMemento().getLives() > 0) {
                    generateNextFruits(time);
                }
                setGameOver();
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    private void generateNextFruits(Timeline time) {
        gamePlayWindow.getRotateTransitions().clear();
        gamePlayWindow.getGameObjects().clear();
        setRandom();
        time.setCycleCount(getRandom());
        setloaded(false);
        gamePlayWindow.setTimeBetweenFruits(0);
        time.play();
    }

    private void showSlicedImage(GameObject gameObject, Image image, int width, int height) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setLayoutX(gameObject.getXLocation());
        imageView.setLayoutY(gameObject.getYLocation());
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        gamePlayWindow.getPane().getChildren().add(imageView);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event1 -> {
            gamePlayWindow.getPane().getChildren().remove(imageView);
        });
        pause.play();
    }

    public void setGameOver() {
        if (gameActions.getGameMemento().getLives() == 0) {
            for (Timeline timeline1 : gamePlayWindow.getTimelines()) {
                timeline1.stop();
            }
            gamePlayWindow.getPane().getChildren().clear();
            if (gameActions.getGameMemento().getCurrentScore() > gameActions.getGameMemento().getHighestScore())
                gameActions.getGameMemento().setHighestScore(gameActions.getGameMemento().getCurrentScore());
            Bomb fatalBomb = null;
            try {
                fatalBomb = new FatalBomb();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = gamePlayWindow.addImageToPane(gamePlayWindow.getPane(), fatalBomb.getSlicedImage(), 225, 132, 250, 300);
            imageView.setPreserveRatio(false);
            gamePlayWindow.moveToNextScene();
        }
    }

    public void setRandom() {
        Random random = new Random();
        rand = random.nextInt(3) + 3;
    }

    public int getRandom() {
        return rand;
    }

    public GameAction getGameActions() {
        return gameActions;
    }

    public boolean getLoaded() {
        return loaded;
    }

    public void setloaded(boolean b) {
        this.loaded = b;
    }

    public int getTempHighScore() {
        return tempHighScore;
    }

    public int getTempCurrentScore() {
        return tempCurrentScore;
    }

}