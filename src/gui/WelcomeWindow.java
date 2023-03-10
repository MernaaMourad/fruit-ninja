package gui;

import gameActions.GameAction;
import gameEngine.GameEngine;
import gameObjects.fruits.Fruit;
import gameObjects.fruits.FruitFactory;
import gameObjects.fruits.Images;
import javafx.animation.PauseTransition;
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
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WelcomeWindow extends GuiController {

    private Scene scene;
    private final Stage stage;

    private GameEngine gameEngine;
    private GamePlayWindow gamePlayWindow;

    private Pane pane;
    private Fruit fruitNewGame;
    private Fruit fruitLoadGame;
    private final SoundTrack soundTrack;

    private long tStart;
    private int flag;

    public WelcomeWindow(Stage stage, SoundTrack soundTrack) {
        this.stage = stage;
        this.soundTrack = soundTrack;
        this.pane = new Pane();
    }

    public void prepareScene() throws IOException, ParserConfigurationException, SAXException {
        GameAction gameActions = GameAction.getInstance();
        gameActions.loadGame();
        gameEngine = new GameEngine();

        setBackGround(pane);

        createCanvas(this, pane, 1);

        BufferedImage bufferedImage[]= Images.getBufferedImage();
        Image image = SwingFXUtils.toFXImage(bufferedImage[19], null);
        addImageToPane(pane, image, 200, 30, 200, 350);

        FruitFactory fruitFactory = new FruitFactory();

        fruitNewGame = fruitFactory.create("apple");
        fruitNewGame.setImageViewLocation(220, 300);
        setRotation(fruitNewGame.getImageView());
        pane.getChildren().add(fruitNewGame.getImageView());

        Label labelClassicMode = new Label("Classic Mode");
        setLabels(pane, labelClassicMode, 180, 400);

        fruitLoadGame = fruitFactory.create("orange");
        fruitLoadGame.setImageViewLocation(420, 300);
        setRotation(fruitLoadGame.getImageView());
        pane.getChildren().add(fruitLoadGame.getImageView());

        Label labelLoadGame = new Label("Load Game");
        setLabels(pane, labelLoadGame,405, 400);

        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bufferedImage[16], null));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        Button soundButton = new Button("", imageView);
        setButtons(pane, soundButton, 595, 20);
        soundButton.setOnAction(e -> {
            if (!soundTrack.isSound()) {
                soundTrack.setSound(true);
                soundTrack.playGameSound();
            } else {
                soundTrack.setSound(false);
                soundTrack.playGameSound();
            }
        });

        Button resetButton = new Button("Reset");
        setButtons(pane, resetButton, 680, 20);
        resetButton.setOnAction(e -> {
            try {
                gameActions.resetGame();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button exitButton = new Button("Exit");
        setButtons(pane, exitButton, 640, 20);
        exitButton.setOnAction(e -> System.exit(0));

        scene = new Scene(pane, 750, 475);

    }

    public void setEvents(Canvas canvas, GraphicsContext graphicsContext) {

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (System.currentTimeMillis() <= tStart + 150 && event.getY() < canvas.getHeight() && event.getX() < canvas.getWidth() && event.getX() > 0 && event.getX() > 0) {

                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();

                if (flag != 1 && fruitNewGame.isSliced() == null && (event.getX() >= fruitNewGame.getImageView().getLayoutX()) && (event.getX() <= fruitNewGame.getImageView().getLayoutX() + fruitNewGame.getImageView().getFitWidth()) && (event.getY() >= fruitNewGame.getImageView().getLayoutY()) && (event.getY() <= fruitNewGame.getImageView().getLayoutY() + fruitNewGame.getImageView().getFitHeight())) {
                    flag = 1;
                    showSlicedImage(fruitNewGame, true);
                } else if (flag != 1 && fruitLoadGame.isSliced() == null && (event.getX() >= fruitLoadGame.getImageView().getLayoutX()) && (event.getX() <= fruitLoadGame.getImageView().getLayoutX() + fruitLoadGame.getImageView().getFitWidth()) && (event.getY() >= fruitLoadGame.getImageView().getLayoutY()) && (event.getY() <= fruitLoadGame.getImageView().getLayoutY() + fruitLoadGame.getImageView().getFitHeight())) {
                    flag = 1;
                    File fp = new File("data.xml");
                    if (fp.length() > 60) {
                        showSlicedImage(fruitLoadGame, false);
                    } else {
                        flag = 0;
                        JOptionPane.showMessageDialog(null, "File is empty !");
                    }
                }
            } else {
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                graphicsContext.beginPath();
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
                tStart = System.currentTimeMillis();
            }
        });

    }

    private void showSlicedImage(Fruit fruit, boolean flag){

        SoundTrack sliceSound = new SoundTrack();
        sliceSound.playSliceSound();
        fruit.slice();
        pane.getChildren().remove(fruit.getImageView());

        addImageToPane(pane, fruit.getSlicedImage(), (int) fruit.getImageView().getLayoutX(), (int) fruit.getImageView().getLayoutY(), 80, 80);
        addImageToPane(pane, fruit.getSlicedImage(), (int) fruit.getImageView().getLayoutX() + 90, (int) fruit.getImageView().getLayoutY(), 80, 80);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event1 -> {
            gamePlayWindow = new GamePlayWindow(stage, gameEngine, soundTrack);
            if(flag == true){
                gameEngine.setGamePlayWindow(gamePlayWindow, 0);
            } else {
                gameEngine.setGamePlayWindow(gamePlayWindow, 1);
            }
            gameEngine.addObserver(gamePlayWindow);

            try {
                gamePlayWindow.prepareScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
            stage.setScene(gamePlayWindow.getScene());
            stage.show();
        });
        pause.play();

    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void setStartTime(long tStart) {
        this.tStart = tStart;
    }

}