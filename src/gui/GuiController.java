package gui;

import gameObjects.fruits.Images;
import javafx.animation.RotateTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public abstract class GuiController {

    private long tStart;

    public void setLabels(Pane pane, Label label, int XPos, int YPos) {
        label.setTextFill(Color.web("#FF0000"));
        label.setFont(Font.font(30));
        label.setLayoutX(XPos);
        label.setLayoutY(YPos);
        pane.getChildren().add(label);
    }

    public void setButtons(Pane pane, Button button, int XPos, int YPos) {
        button.setLayoutX(XPos);
        button.setLayoutY(YPos);
        pane.getChildren().add(button);
    }

    public Canvas createCanvas(GuiController gui, Pane pane, int flag) {
        Canvas canvas = new Canvas();
        canvas.setHeight(475);
        canvas.setWidth(750);
        pane.getChildren().add(canvas);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext, flag);
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        setEvents(canvas, graphicsContext, gui);
        return  canvas;
    }

    public void setEvents(Canvas canvas, GraphicsContext graphicsContext, GuiController gui) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                    tStart = System.currentTimeMillis();
                });
        if (gui instanceof WelcomeWindow) {
            ((WelcomeWindow) gui).setEvents(canvas, graphicsContext);
            ((WelcomeWindow) gui).setStartTime(tStart);
        } else if (gui instanceof GamePlayWindow) {
            ((GamePlayWindow) gui).setEvents(canvas, graphicsContext);
            ((GamePlayWindow) gui).setStartTime(tStart);
        } else if (gui instanceof ScoreWindow) {
            ((ScoreWindow) gui).setEvents(canvas, graphicsContext);
            ((ScoreWindow) gui).setStartTime(tStart);
        }
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                event -> {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        }
                    }, 1);
                });
    }

    public RotateTransition setRotation(ImageView iv) {
        Duration duration = Duration.millis(2000);
        RotateTransition rotateTransition = new RotateTransition(duration, iv);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setByAngle(360);
        rotateTransition.play();
        return rotateTransition;
    }

    public ImageView addImageToPane(Pane pane, Image image, int XPos, int YPos, int height, int width){
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(XPos);
        imageView.setLayoutY(YPos);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        pane.getChildren().add(imageView);
        return imageView;
    }

    public void setBackGround(Pane pane) throws IOException {

        BufferedImage img[]= Images.getBufferedImage();
        Image image = SwingFXUtils.toFXImage(img[20], null);
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);
        pane.setBackground(background);

    }

    public void initDraw(GraphicsContext gc, int flag) {
        if (flag == 1) {
            gc.setStroke(Color.BLUEVIOLET);
        } else if (flag == 2) {
            gc.setStroke(Color.GOLD);
        } else {
            gc.setStroke(Color.BLUE);
        }
        gc.setLineWidth(5);
    }

}
