package gui;

import gameActions.GameAction;
import gameEngine.GameEngine;
import gameObjects.gameObject.ENUM;
import gameObjects.gameObject.GameObject;
import gameEngine.IObserver;
import gameObjects.fruits.Images;
import javafx.animation.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;

public class GamePlayWindow extends GuiController implements IObserver {

    private Scene scene;
    private Stage stage;

    private ArrayList<GameObject> gameObjects;
    private ArrayList<ImageView> hearts;
    private ArrayList<Timeline> timelines;
    private ArrayList<RotateTransition> rotateTransitions;

    private int timeBetweenFruits;
    private int score;
    private long tStart;
    private long comboTime;

    private GameEngine gameEngine;
    private Timeline timeline;
    private Pane pane;

    private boolean paused;
    private int secondsCount;
    private int minuteCount;
    private SoundTrack soundTrack;

    private Label scoreLabel;
    private Label labelHighScore;
    private Label labelDifficultyLevel;
    private Label stopwatch;
    private Button soundButton;
    private Button save;

    private int temp;
    private int comboCount;

    public GamePlayWindow(Stage stage, GameEngine gameEngine, SoundTrack soundTrack) {
        this.stage = stage;
        this.gameEngine = gameEngine;
        this.soundTrack = soundTrack;
        gameObjects = new ArrayList<>();
        pane = new Pane();
        timelines = new ArrayList<>();
        rotateTransitions = new ArrayList<>();
        hearts = new ArrayList<>();
        timeline = new Timeline();
        save = new Button("Save");
    }

    public void prepareScene() throws IOException {

        setHeartImages();
        gameEngine.setGameActions();

        setBackGround(pane);

        Canvas canvas = createCanvas(this, pane, 2);

        scoreLabel = new Label("Score: " + gameEngine.getTempCurrentScore());
        setLabels(pane, scoreLabel, 10, 10);

        labelHighScore = new Label("Best Score: " + gameEngine.getTempHighScore());
        setLabels(pane, labelHighScore, 10, 40);

        labelDifficultyLevel = new Label("Difficulty Level: " + gameEngine.getGameActions().getGameMemento().getDifficulty());
        setLabels(pane, labelDifficultyLevel, 10, 75);

        stopwatch = new Label("0:00");
        setLabels(pane, stopwatch, 620, 50);

        Timeline stopwatchTimeline = new Timeline();
        stopwatchTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), t -> {
            if (secondsCount < 10)
                stopwatch.setText(minuteCount + ":0" + secondsCount++);
            else if (secondsCount >= 10 && secondsCount < 60)
                stopwatch.setText(minuteCount + ":" + secondsCount++);
            else if (secondsCount == 60) {
                secondsCount = 0;
                stopwatch.setText((++minuteCount) + ":00");
            }
        }));
        stopwatchTimeline.setCycleCount(Timeline.INDEFINITE);
        stopwatchTimeline.play();

        BufferedImage img[] = Images.getBufferedImage();
        ImageView imageViewSound = new ImageView(SwingFXUtils.toFXImage(img[16], null));
        imageViewSound.setFitHeight(20);
        imageViewSound.setFitWidth(20);
        soundButton = new Button("", imageViewSound);
        setButtons(pane, soundButton, 440, 20);
        soundButton.setVisible(false);
        soundButton.setOnAction(e -> {
            if (!soundTrack.isSound()) {
                soundTrack.setSound(true);
            } else {
                soundTrack.setSound(false);
            }
            soundTrack.playGameSound();
        });

        Button exitButton = new Button("Exit");
        setButtons(pane, exitButton, 545, 20);
        exitButton.setVisible(false);
        exitButton.setOnAction(e -> {
            System.exit(0);
        });

        ImageView imageViewPause = new ImageView(SwingFXUtils.toFXImage(img[17], null));
        imageViewPause.setFitHeight(20);
        imageViewPause.setFitWidth(20);
        Button pauseButton = new Button("", imageViewPause);
        setButtons(pane, pauseButton, 380, 20);
        pauseButton.setOnAction(e -> {
            if (!paused) {
                for (Timeline timeline : timelines) {
                    timeline.pause();
                }
                for (RotateTransition rotateTransition : rotateTransitions) {
                    rotateTransition.pause();
                }
                stopwatchTimeline.pause();
                canvas.setDisable(true);
                paused = true;
                soundButton.setVisible(true);
                save.setVisible(true);
                exitButton.setVisible(true);
            } else {
                for (Timeline timeline : timelines) {
                    timeline.play();
                }
                for (RotateTransition rotateTransition : rotateTransitions) {
                    rotateTransition.play();
                }
                stopwatchTimeline.play();
                canvas.setDisable(false);
                paused = false;
                soundButton.setVisible(false);
                save.setVisible(false);
                exitButton.setVisible(false);
            }
        });

        setButtons(pane, save, 490, 20);
        save.setVisible(false);
        save.setOnAction(event -> saveGui());

        scene = new Scene(pane, 750, 475);

        moveObjects();

    }

    private void moveObjects() {

        if (gameEngine.getLoaded() == false || gameEngine.getGameActions().getGameMemento().getGameObjects().size() == 0) {
            gameEngine.setRandom();
            setTimeBetweenFruits(0);
        } else {
            setTimeBetweenFruits(1);
        }
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(getTimeBetweenFruits()), t -> {
            if (gameEngine.getLoaded() == false || gameEngine.getGameActions().getGameMemento().getGameObjects().size() == 0) {
                try {
                    setTimeBetweenFruits(0);
                    randomObjects();
                    gameEngine.setloaded(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (gameEngine.getLoaded() == true && gameEngine.getGameActions().getGameMemento().getGameObjects().size() > 0) {
                GameObject gameObject = gameObjects.get(temp);
                BufferedImage bufferedImage[] = new BufferedImage[10];
                try {
                    bufferedImage = Images.getBufferedImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (gameObject.getObjectType() == ENUM.Apple) {
                    gameObject.setBufferedImages(bufferedImage[0]);
                    gameObject.setBufferedImages(bufferedImage[1]);
                    gameObject.setImageView(80, 80);
                } else if (gameObject.getObjectType() == ENUM.Orange) {
                    gameObject.setBufferedImages(bufferedImage[2]);
                    gameObject.setBufferedImages(bufferedImage[3]);
                    gameObject.setImageView(80, 80);
                } else if (gameObject.getObjectType() == ENUM.Watermelon) {
                    gameObject.setBufferedImages(bufferedImage[4]);
                    gameObject.setBufferedImages(bufferedImage[5]);
                    gameObject.setImageView(100, 100);
                } else if (gameObject.getObjectType() == ENUM.DangerousBomb) {
                    gameObject.setBufferedImages(bufferedImage[6]);
                    gameObject.setBufferedImages(bufferedImage[7]);
                    gameObject.setImageView(100, 100);
                } else if (gameObject.getObjectType() == ENUM.FatalBomb) {
                    gameObject.setBufferedImages(bufferedImage[8]);
                    gameObject.setBufferedImages(bufferedImage[9]);
                    gameObject.setImageView(100, 100);
                } else if (gameObject.getObjectType() == ENUM.DoubleScoreBanana) {
                    gameObject.setBufferedImages(bufferedImage[10]);
                    gameObject.setBufferedImages(bufferedImage[11]);
                    gameObject.setImageView(100, 100);
                } else if (gameObject.getObjectType() == ENUM.MagicBean) {
                    gameObject.setBufferedImages(bufferedImage[12]);
                    gameObject.setImageView(100, 100);
                }
                gameObject.getImageView().setLayoutX(gameObject.getXLocation());
                gameObject.getImageView().setLayoutY(gameObject.getYLocation());
                pane.getChildren().add(gameObject.getImageView());
                gameEngine.moveEngine(gameObject, timeline);
                temp++;
            }
        }));
        timeline.setCycleCount(gameEngine.getRandom());
        timeline.play();

    }

    public void randomObjects() throws IOException {
        GameObject gameObject = gameEngine.getGameActions().createGameObject();
        pane.getChildren().add(gameObject.getImageView());
        gameObjects.add(gameObject);
        gameEngine.moveEngine(gameObject, timeline);
    }

    public void setEvents(Canvas canvas, GraphicsContext graphicsContext) {
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (System.currentTimeMillis() <= tStart + 150 && event.getY() < canvas.getHeight() && event.getX() < canvas.getWidth() && event.getX() > 0 && event.getX() > 0) {
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
                if(comboTime==0)
                {
                    comboTime=System.currentTimeMillis();
                }
                try {
                    gameEngine.sliceEngine(gameObjects, event, timeline);
                } catch (InterruptedException | IOException | NullPointerException e) {
                    e.printStackTrace();
                }
                scoreLabel.setText("Score: " + gameEngine.getGameActions().getGameMemento().getCurrentScore());

            } else {
                if (System.currentTimeMillis() >= comboTime + 1000) {
                    comboCount = 0;
                    comboTime = 0;
                }
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                graphicsContext.beginPath();
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
                tStart = System.currentTimeMillis();
            }
        });

    }

    @Override
    public void update(int score, int highScore, int remainingLives, boolean flag) {
        if (flag) {
            this.score = score;
            if (this.score >= highScore)
                sethScore(highScore);
        } else {
            showHeartImages(remainingLives);
        }
    }

    public void saveGui() {
        GameAction gameActions = GameAction.getInstance();
        gameActions.setGameMemento(gameActions.getGameMemento().getDifficulty(), gameActions.getGameMemento().getCurrentScore(), gameActions.getGameMemento().getHighestScore(), gameActions.getGameMemento().getLives());
        gameEngine.getGameActions().getGameMemento().setGameObjects(gameObjects);

        try {
            gameEngine.getGameActions().saveGame();
        } catch (TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void moveToNextScene() {

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event1 -> {
            ScoreWindow scoreGui = new ScoreWindow(stage, gameEngine, soundTrack);
            try {
                scoreGui.prepareScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameEngine.addObserver(scoreGui);
            gameEngine.notifyObservers(gameEngine.getGameActions().getGameMemento().getCurrentScore(), gameEngine.getGameActions().getGameMemento().getHighestScore(), gameEngine.getGameActions().getGameMemento().getLives(), true);
            stage.close();
            stage.setScene(scoreGui.getScene());
            stage.show();
        });
        pause.play();
    }

    public void showHeartImages(int remainingLives) {
        for (int i = 0; i < 3; i++) {
            hearts.get(i).setVisible(false);
        }
        int temp = 2;
        for (int i = 0; i < remainingLives; i++) {
            hearts.get(temp--).setVisible(true);
        }
    }

    private void setHeartImages() throws IOException {
        int temp = 0;
        BufferedImage img[] = Images.getBufferedImage();
        for (int i = 0; i < 3; i++) {
            Image image = SwingFXUtils.toFXImage(img[13], null);
            ImageView imageView = addImageToPane(pane, image, 600 + temp, 20, 30, 30);
            imageView.setVisible(false);
            temp += 50;
            hearts.add(imageView);
        }

    }

    public void setTimeBetweenFruits(int x) {
        if (x == 1) {
            this.timeBetweenFruits = 1;
        } else {
            Random random = new Random();
            if (gameEngine.getGameActions().getGameMemento().getCurrentScore() < 15)
                this.timeBetweenFruits = random.nextInt(300) + 200;
            else if (gameEngine.getGameActions().getGameMemento().getCurrentScore() >= 15 && gameEngine.getGameActions().getGameMemento().getCurrentScore() < 25)
                this.timeBetweenFruits = random.nextInt(200) + 100;
            else if (gameEngine.getGameActions().getGameMemento().getCurrentScore() > 25)
                this.timeBetweenFruits = random.nextInt(100);
        }
    }

    public void setStartTime(long tStart) {
        this.tStart = tStart;
    }

    public int getTimeBetweenFruits() {
        return timeBetweenFruits;
    }

    public ArrayList<Timeline> getTimelines() {
        return timelines;
    }

    public ArrayList<RotateTransition> getRotateTransitions() {
        return rotateTransitions;
    }

    public Pane getPane() {
        return pane;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public boolean isPaused() {
        return paused;
    }

    public void sethScore(int hScore) {
        this.labelHighScore.setText("Best Score: " + hScore);
    }

    public Scene getScene() {
        return scene;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public Label getLabelDifficultyLevel() {
        return labelDifficultyLevel;
    }

    public int getComboCount() {
        return comboCount;
    }

    public void setComboCount() {
        comboCount++;
    }

}
