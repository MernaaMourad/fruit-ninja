package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FruitNinja extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        SoundTrack soundTrack = new SoundTrack();
        soundTrack.setSound(true);
        soundTrack.playGameSound();

        WelcomeWindow welcomeWindow = new WelcomeWindow(primaryStage, soundTrack);
        welcomeWindow.prepareScene();
        welcomeWindow.setScene(welcomeWindow.getScene());

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(welcomeWindow.getScene());
        primaryStage.show();

    }
}
