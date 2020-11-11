package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartController {

    @FXML
    private Button startButton;

    private MainScreen mainScreen;

    @FXML
    public void handleStartButton(ActionEvent event) {
        mainScreen.start();
    }

	public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
	}
}

