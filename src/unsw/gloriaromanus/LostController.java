package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LostController {

    @FXML
    private TextField playerName;

    private MainScreen mainScreen;

    @FXML
    private void clickedNextButton() {
        mainScreen.returnToMain();
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

	public void displayPlayer(Player p) {
        playerName.setText(p.getFaction() + " has lost all their provinces. GG!");
	}
}
