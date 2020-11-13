package unsw.gloriaromanus;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartController {

    @FXML
    private Button startButton;
    @FXML
    private Button loadButton;

    private MainScreen mainScreen;
    private Audio audio;

    @FXML
    public void handleStartButton(ActionEvent event) throws IOException {
        mainScreen.start();
    }

    @FXML
    public void handleLoadButton(ActionEvent event) throws IOException {
        mainScreen.load();
    }

	public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
	}

    public void setAudio(Audio audio) {
        this.audio = audio;
    }
}

