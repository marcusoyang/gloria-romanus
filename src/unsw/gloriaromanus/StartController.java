package unsw.gloriaromanus;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class StartController {

    @FXML
    private Button startButton;
    @FXML
    private TextField numPlayers;
    @FXML
    private Button loadButton;
    @FXML
    private TextField loadFilename;

    private MainScreen mainScreen;
    private Audio audio;

    @FXML
    public void handleStartButton(ActionEvent event) throws IOException {
        mainScreen.start(numPlayers.getText());
    }

    @FXML
    public void handleLoadButton(ActionEvent event) throws IOException {
        mainScreen.load(loadFilename.getText());
    }

	public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
	}

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public void clearTextFields() {
        numPlayers.clear();
        loadFilename.clear();
    }
}

