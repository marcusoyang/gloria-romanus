package unsw.gloriaromanus;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainScreen {

    private Stage stage;
    private String title;
    private GloriaRomanusController controller;
    private Scene scene;

    public MainScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Gloria Romanus";

        controller = new GloriaRomanusController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(controller);

        // load into a Parent node
        Parent root = loader.load();

        scene = new Scene(root, 1600, 900);
    }

    public void start(int numPlayers) throws IOException {
        if (controller.getFactionsSize() < numPlayers) { return; }
        controller.newGame(numPlayers);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void load(String loadFilename) throws IOException {
        controller.loadGame(loadFilename);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void returnToMain() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void purchaseUnit(String unitType, int numTroops) throws IOException {
        controller.requestTraining(unitType, numTroops);
    }

    public GloriaRomanusController getController() {
        return controller;
    }

	public void printToTerminal(String msg) {
        controller.printMessageToTerminal(msg);
    }
    
    public void setControllerFactions(ArrayList<String> factions) {
        controller.setFactions(factions);
    }

    public void invade(ArrayList<Integer> ids) throws IOException {
        controller.invade(ids);
    }
}

