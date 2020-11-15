package unsw.gloriaromanus;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

    @FXML
    private CheckBox Romans;
    @FXML
    private CheckBox Carthaginians;
    @FXML
    private CheckBox Gauls;
    @FXML
    private CheckBox CelticBritons;
    @FXML
    private CheckBox Spanish;
    @FXML
    private CheckBox Numidians;
    @FXML
    private CheckBox Egyptians;
    @FXML
    private CheckBox SeleucidEmpire;
    @FXML
    private CheckBox Pontus;
    @FXML
    private CheckBox Amenians;
    @FXML
    private CheckBox Parthians;
    @FXML
    private CheckBox Germanics;
    @FXML
    private CheckBox GreekCityStates;
    @FXML
    private CheckBox Macedonians;
    @FXML
    private CheckBox Thracians;
    @FXML
    private CheckBox Dacians;

    private MainScreen mainScreen;
    private Audio audio;

    @FXML
    public void handleStartButton(ActionEvent event) throws IOException {
        if (numPlayers.getText().isEmpty()) {
            return;
        }
        initializeFactions();
        mainScreen.start(Integer.valueOf(numPlayers.getText()));
    }

    private void initializeFactions() {
        ArrayList<String> factionList = new ArrayList<String>();
        if (Romans.isSelected()) { factionList.add(Romans.getText()); }
        if (Carthaginians.isSelected()) { factionList.add(Carthaginians.getText()); }
        if (Gauls.isSelected()) { factionList.add(Gauls.getText()); }
        if (CelticBritons.isSelected()) { factionList.add(CelticBritons.getText()); }
        if (Spanish.isSelected()) { factionList.add(Spanish.getText()); }
        if (Numidians.isSelected()) { factionList.add(Numidians.getText()); }
        if (Egyptians.isSelected()) { factionList.add(Egyptians.getText()); }
        if (SeleucidEmpire.isSelected()) { factionList.add(SeleucidEmpire.getText()); }
        if (Pontus.isSelected()) { factionList.add(Pontus.getText()); }
        if (Amenians.isSelected()) { factionList.add(Amenians.getText()); }
        if (Parthians.isSelected()) { factionList.add(Parthians.getText()); }
        if (Germanics.isSelected()) { factionList.add(Germanics.getText()); }
        if (GreekCityStates.isSelected()) { factionList.add(GreekCityStates.getText()); }
        if (Macedonians.isSelected()) { factionList.add(Macedonians.getText()); }
        if (Thracians.isSelected()) { factionList.add(Thracians.getText()); }
        if (Dacians.isSelected()) { factionList.add(Dacians.getText()); }
        mainScreen.setControllerFactions(factionList);
    }

    @FXML
    public void handleLoadButton(ActionEvent event) throws IOException {
        mainScreen.load(loadFilename.getText());
    }

	public void setMainScreen(MainScreen mainScreen2) {
        this.mainScreen = mainScreen2;
	}

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public void clearTextFields() {
        numPlayers.clear();
        loadFilename.clear();
    }
}

