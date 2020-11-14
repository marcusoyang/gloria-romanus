package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RecruitController {

    @FXML
    private TextField numTroops;

    private MainScreen mainScreen;

    @FXML
    private void clickedCloseMenuButton() throws IOException {
        mainScreen.returnToMain();
    }

    @FXML
    private void clickedRecruitLegionary() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("legionary", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    @FXML
    private void clickedRecruitBerserker() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("berserker", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    @FXML
    private void clickedRecruitPikemen() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("pikemen", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    @FXML
    private void clickedRecruitHoplite() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("hoplite", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    @FXML
    private void clickedRecruitJavelinSkirmisher() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("javelin-skirmisher", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    @FXML
    private void clickedRecruitElephant() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("elephant", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    @FXML
    private void clickedRecruitHorseArcher() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("horse_archer", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    @FXML
    private void clickedRecruitDruid() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("druid", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    @FXML
    private void clickedRecruitCatapult() throws IOException {
        if (isValidAmount()) {
            mainScreen.purchaseUnit("catapult", Integer.valueOf(numTroops.getText()));
            mainScreen.returnToMain();
        }
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    
    private boolean isValidAmount() {
        if (numTroops.getText().isEmpty()) {
            mainScreen.printToTerminal("Invalid Amount.");
            mainScreen.returnToMain();
            return false;
        }
        return true;
    }

	public void clearTextFields() {
        numTroops.clear();
	}
}
