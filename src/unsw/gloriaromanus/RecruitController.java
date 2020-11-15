package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RecruitController {

    @FXML
    private TextField numTroops;
    @FXML
    private TextField legionaryCost;
    @FXML
    private TextField berserkerCost;
    @FXML
    private TextField pikemenCost;
    @FXML
    private TextField hopeliteCost;
    @FXML
    private TextField javelinSkirmisherCost;
    @FXML
    private TextField elephantCost;
    @FXML
    private TextField horseArcherCost;
    @FXML
    private TextField druidCost;
    @FXML
    private TextField catapultCost;

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

	public void loadPrices(String path) throws IOException {
        String configString = Files.readString(Paths.get(path));
        JSONObject config = new JSONObject(configString);
        legionaryCost.setText(config.getJSONObject("legionary").optString("cost"));
        berserkerCost.setText(config.getJSONObject("berserker").optString("cost"));
        pikemenCost.setText(config.getJSONObject("pikemen").optString("cost"));
        hopeliteCost.setText(config.getJSONObject("hoplite").optString("cost"));
        javelinSkirmisherCost.setText(config.getJSONObject("javelin-skirmisher").optString("cost"));
        elephantCost.setText(config.getJSONObject("elephant").optString("cost"));
        horseArcherCost.setText(config.getJSONObject("horse_archer").optString("cost"));
        druidCost.setText(config.getJSONObject("druid").optString("cost"));
        catapultCost.setText(config.getJSONObject("catapult").optString("cost"));
	}
}
