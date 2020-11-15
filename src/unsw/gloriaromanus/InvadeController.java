package unsw.gloriaromanus;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class InvadeController {
    
    @FXML
    private TextField n1;
    @FXML
    private TextField n2;
    @FXML
    private TextField n3;
    @FXML
    private TextField n4;
    @FXML
    private TextField n5;
    @FXML
    private TextField n6;
    @FXML
    private TextField n7;
    @FXML
    private TextField n8;
    @FXML
    private TextField n9;

    @FXML
    private TextField unit1;
    @FXML
    private TextField unit2;
    @FXML
    private TextField unit3;
    @FXML
    private TextField unit4;
    @FXML
    private TextField unit5;
    @FXML
    private TextField unit6;
    @FXML
    private TextField unit7;
    @FXML
    private TextField unit8;
    @FXML
    private TextField unit9;

    private MainScreen mainScreen;
    private ArrayList<TextField> numTroopFieldList;
    private ArrayList<TextField> troopNameFieldList;
    private ArrayList<Integer> allIDs;
    private ArrayList<Integer> invadingIDs;

    @FXML
    private void clickedCloseMenuButton() throws IOException {
        mainScreen.returnToMain();
    }

    @FXML
    private void clickedInvade() throws IOException {
        mainScreen.returnToMain();
    }

    @FXML
    private void u1() throws IOException {
        addToInvadingList(1);
    }

    @FXML
    private void u2() throws IOException {
        addToInvadingList(2);
    }

    @FXML
    private void u3() throws IOException {
        addToInvadingList(3);
    }

    @FXML
    private void u4() throws IOException {
        addToInvadingList(4);
    }

    @FXML
    private void u5() throws IOException {
        addToInvadingList(5);
    }

    @FXML
    private void u6() throws IOException {
        addToInvadingList(6);
    }

    @FXML
    private void u7() throws IOException {
        addToInvadingList(7);
    }

    @FXML
    private void u8() throws IOException {
        addToInvadingList(8);
    }

    @FXML
    private void u9() throws IOException {
        addToInvadingList(9);
    }

    private void addToInvadingList(int i) {
        invadingIDs.add(allIDs.get(i));
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

	public void loadUnits(ArrayList<Unit> units) {
        int i = 0;
        for (Unit u : units) {
            String numTroops = String.valueOf(u.getNumTroops());
            numTroopFieldList.get(i).setText(numTroops);
            troopNameFieldList.get(i).setText(u.getType());
        }
    }
    
    public void numTroopFieldList() {
        numTroopFieldList.add(n1);
        numTroopFieldList.add(n2);
        numTroopFieldList.add(n3);
        numTroopFieldList.add(n4);
        numTroopFieldList.add(n5);
        numTroopFieldList.add(n6);
        numTroopFieldList.add(n7);
        numTroopFieldList.add(n8);
        numTroopFieldList.add(n9);

        troopNameFieldList.add(unit1);
        troopNameFieldList.add(unit2);
        troopNameFieldList.add(unit3);
        troopNameFieldList.add(unit4);
        troopNameFieldList.add(unit5);
        troopNameFieldList.add(unit6);
        troopNameFieldList.add(unit7);
        troopNameFieldList.add(unit8);
        troopNameFieldList.add(unit9);
    }
}
