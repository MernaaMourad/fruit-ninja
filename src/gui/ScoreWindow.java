package gui;

import gameEngine.GameEngine;
import gameEngine.IObserver;
import gameObjects.fruits.Fruit;
import gameObjects.fruits.FruitFactory;
import gameObjects.fruits.Images;
import javafx.animation.PauseTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class ScoreWindow extends GuiController implements IObserver {

    private Stage stage;
    private Scene scene;

    private BufferedImage[] img;
    private WelcomeWindow welcomeWindow;
    private GameEngine gameEngine;
    private SoundTrack soundTrack;

    private Pane pane;

    private Label labelScore;
    private Label labelHighScore;

    private Fruit fruit;
    private long tStart;

    public ScoreWindow(Stage stage, GameEngine gameEngine, SoundTrack soundTrack) {
        this.stage = stage;
        this.gameEngine = gameEngine;
        this.soundTrack = soundTrack;
        this.pane = new Pane();
        this.labelScore = new Label("Score: 0");
        labelHighScore = new Label("High Score: 0");
    }

    public void prepareScene() throws IOException {

        img=Images.getBufferedImage();

        setBackGround(pane);

        createCanvas(this, pane, 3);
        setLabels(pane, labelScore, 320, 200);
        setLabels(pane, labelHighScore, 320, 240);

        BufferedImage img[]= Images.getBufferedImage();
        Image image = SwingFXUtils.toFXImage(img[14], null);
        addImageToPane(pane, image, 50, 150, 250, 150);

        FruitFactory fruitFactory = new FruitFactory();
        fruit = fruitFactory.create("watermelon");
        fruit.setImageViewLocation(550, 350);
        pane.getChildren().add(fruit.getImageView());
        setRotation(fruit.getImageView());

        scene = new Scene(pane, 750, 475);

    }

    @Override
    public void update(int score, int highScore, int remainingLives, boolean flag) {
        labelScore.setText("Score: " + score);
        labelHighScore.setText("High Score: " + highScore);
    }

    public void setEvents(Canvas canvas, GraphicsContext graphicsContext) {

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (System.currentTimeMillis() <= tStart + 150 && event.getY() < canvas.getHeight() && event.getX() < canvas.getWidth() && event.getX() > 0 && event.getX() > 0) {
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();

                if (fruit.isSliced() == null && (event.getX() >= fruit.getImageView().getLayoutX()) && (event.getX() <= fruit.getImageView().getLayoutX() + fruit.getImageView().getFitWidth()) && (event.getY() >= fruit.getImageView().getLayoutY()) && (event.getY() <= fruit.getImageView().getLayoutY() + fruit.getImageView().getFitHeight())) {
                    SoundTrack sliceSound = new SoundTrack();
                    sliceSound.playSliceSound();
                    fruit.slice();
                    pane.getChildren().remove(fruit.getImageView());

                    Image image = SwingFXUtils.toFXImage(img[15], null);
                    addImageToPane(pane, image, (int) fruit.getImageView().getLayoutX(), (int) fruit.getImageView().getLayoutY(), 120, 120);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event1 -> {

                        welcomeWindow = new WelcomeWindow(stage, soundTrack);
                        if (gameEngine.getGameActions().getGameMemento().getCurrentScore() == gameEngine.getGameActions().getGameMemento().getHighestScore()) {
                            gameEngine.getGameActions().getGameMemento().setLives(3);
                            gameEngine.getGameActions().getGameMemento().setDifficulty(1);
                            gameEngine.getGameActions().getGameMemento().setCurrentScore(0);
                            gameEngine.getGameActions().getGameMemento().getGameObjects().clear();
                            try {
                                gameEngine.getGameActions().saveGame();
                            } catch (ParserConfigurationException | TransformerException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            welcomeWindow.prepareScene();
                        } catch (IOException | ParserConfigurationException | SAXException e) {
                            e.printStackTrace();
                        }

                        stage.close();
                        stage.setScene(welcomeWindow.getScene());
                        stage.show();
                    });
                    pause.play();

                }
            } else {
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                graphicsContext.beginPath();
                (graphicsContext).lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
                tStart = System.currentTimeMillis();
            }
        });

    }

    public Scene getScene() {
        return scene;
    }

    public void setStartTime(long tStart) {
        this.tStart = tStart;
    }

}
