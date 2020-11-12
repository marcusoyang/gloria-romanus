package unsw.gloriaromanus;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Audio {

    private MediaPlayer mediaPlayer;
    
    public Audio() {
        String musicFile = "src/unsw/gloriaromanus/audio/bustlingAfternoonOfMondstadt.wav";

        Media defaultBackground = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(defaultBackground);
    }

    public void playAudio() {
        mediaPlayer.play();
    }

    public void changeVolume(double vol) {
        mediaPlayer.setVolume(vol);
    }
}
