package unsw.gloriaromanus;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Audio {

    private static final double DEFAULT_VOL = 0.5;
	private MediaPlayer mediaPlayer;
    
    public Audio() {
        String musicFile = "src/unsw/gloriaromanus/media/bustlingAfternoonOfMondstadt.mp3";

        Media defaultBackground = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(defaultBackground);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        changeVolume(DEFAULT_VOL);
    }

    public void playAudio() {
        mediaPlayer.play();
    }

    public void changeVolume(double vol) {
        mediaPlayer.setVolume(vol);
    }

	public static double getDefaultVol() {
		return DEFAULT_VOL;
	}
}
