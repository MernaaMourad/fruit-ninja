package gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundTrack {

    private boolean sound;
    private Media gamedMedia, sliceMedia;
    private MediaPlayer mediaPlayerGame, mediaPlayerSlice;

    public SoundTrack() {

        gamedMedia = new Media(new File("Soundtrack.mp3").toURI().toString());
        mediaPlayerGame = new MediaPlayer(gamedMedia);
        mediaPlayerGame.setCycleCount(MediaPlayer.INDEFINITE);

        sliceMedia = new Media(new File("SliceSound.mp3").toURI().toString());
        mediaPlayerSlice = new MediaPlayer(sliceMedia);

    }

    public void playGameSound() {
        if (sound) {
            mediaPlayerGame.play();
        } else {
            mediaPlayerGame.pause();
        }
    }

    public void playSliceSound() {
        mediaPlayerSlice.play();
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isSound() {
        return sound;
    }

}
