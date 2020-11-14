package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

    public void start(String numPlayers) throws IOException {
        controller.newGame(Integer.valueOf(numPlayers));
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
}

